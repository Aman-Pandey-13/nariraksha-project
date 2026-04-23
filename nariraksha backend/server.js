require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const bcrypt = require("bcryptjs");
const nodemailer = require("nodemailer");
const cors = require("cors");

const app = express();
app.use(express.json());
app.use(cors());

/* ================= IMPORT MODEL ================= */
const User = require("./user");

/* ================= DATABASE ================= */
mongoose
  .connect(process.env.MONGO_URI)
  .then(() => console.log("✅ MongoDB Connected"))
  .catch((err) => console.log("❌ DB Error:", err));

/* ================= MAIL SETUP ================= */
const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: process.env.EMAIL_USER,
    pass: process.env.EMAIL_PASS,
  },
});

/* ================= REGISTER ================= */
app.post("/api/register", async (req, res) => {
  console.log("🔥 REGISTER DATA:", req.body);

  try {
    const { name, email, password, phone, contact1, contact2, contact3 } =
      req.body;

    // check existing
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ message: "User already exists" });
    }

    // hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    // create user
    const user = new User({
      name,
      email,
      password: hashedPassword,
      phoneNumber: phone,
      emergencyContacts: [contact1, contact2, contact3],
    });

    await user.save();

    res.json({ message: "Registered Successfully" });
  } catch (err) {
    console.error("❌ REGISTER ERROR:", err);
    res.status(500).json({ message: "Server Error" });
  }
});

/* ================= LOGIN (SEND OTP) ================= */
app.post("/api/login", async (req, res) => {
  console.log("🔥 LOGIN DATA:", req.body);

  try {
    const { email, password } = req.body;

    const user = await User.findOne({ email });
    if (!user) {
      return res.status(400).json({ message: "User not found" });
    }

    const match = await bcrypt.compare(password, user.password);
    if (!match) {
      return res.status(400).json({ message: "Invalid password" });
    }

    // generate OTP
    const otp = Math.floor(100000 + Math.random() * 900000).toString();

    user.otp = otp;
    user.otpExpires = Date.now() + 5 * 60 * 1000;

    await user.save();

    // send email
    await transporter.sendMail({
      from: process.env.EMAIL_USER,
      to: email,
      subject: "Your OTP Code",
      text: `Your OTP is ${otp}`,
    });

    res.json({ message: "OTP Sent" });
  } catch (err) {
    console.error("❌ LOGIN ERROR:", err);
    res.status(500).json({ message: "Server Error" });
  }
});

/* ================= VERIFY OTP ================= */
app.post("/api/verify-otp", async (req, res) => {
  try {
    const { email, otp } = req.body;

    const user = await User.findOne({ email });

    if (!user) {
      return res.status(400).json({ message: "User not found" });
    }

    // ✅ STRICT CHECK
    if (!user.otp || user.otp !== otp) {
      return res.status(400).json({ message: "Invalid OTP" });
    }

    // ✅ EXPIRY CHECK
    if (user.otpExpires < Date.now()) {
      return res.status(400).json({ message: "OTP expired" });
    }

    // ✅ CLEAR OTP AFTER SUCCESS
    user.otp = null;
    user.otpExpires = null;
    await user.save();

    res.json({ message: "OTP Verified Successfully" });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: "Server Error" });
  }
});

// ================== Resend Otp ================= */

app.post("/api/resend-otp", async (req, res) => {
  try {
    const { email } = req.body;

    const user = await User.findOne({ email });

    if (!user) {
      return res.status(400).json({ message: "User not found" });
    }

    // generate new OTP
    const otp = Math.floor(100000 + Math.random() * 900000).toString();

    user.otp = otp;
    user.otpExpires = Date.now() + 5 * 60 * 1000;

    await user.save();

    await transporter.sendMail({
      from: process.env.EMAIL_USER,
      to: email,
      subject: "Resent OTP",
      text: `Your new OTP is ${otp}`,
    });

    res.json({ message: "OTP Resent Successfully" });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: "Server Error" });
  }
});

// GET USER PROFILE
/* ================= GET USER FOR PROFILE ================= */

app.post("/api/get-user", async (req, res) => {
  try {
    const { email } = req.body;

    const user = await User.findOne({ email }).select("-password");

    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }
    res.json({
      name: user.name,
      email: user.email,
      phone: user.phoneNumber, // 🔥 FIX
      contact1: user.emergencyContacts[0] || "",
      contact2: user.emergencyContacts[1] || "",
      contact3: user.emergencyContacts[2] || "",
    });
  } catch (err) {
    res.status(500).json({ message: "Server error" });
  }
});

app.put("/api/update-user", async (req, res) => {
  try {
    const { email, name, phone, contact1, contact2, contact3 } = req.body;

    const user = await User.findOneAndUpdate(
      { email },
      { name, phone, contact1, contact2, contact3 },
      { new: true },
    );

    res.json({ message: "Profile Updated" });
  } catch (err) {
    res.status(500).json({ message: "Server Error" });
  }
});
/* ================= TEST ROUTE ================= */
app.get("/", (req, res) => {
  res.send("NariRaksha Backend Running ✅");
});

/* ================= START SERVER ================= */
const PORT = process.env.PORT || 5000;

app.listen(PORT, "0.0.0.0", () => {
  console.log(`🚀 Server running on port ${PORT}`);
});
