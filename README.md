# Catalog Placements Assignment - Secret Sharing

## 📘 Problem Overview

This project solves a simplified version of **Shamir's Secret Sharing** algorithm.

Given `k` points from a hidden polynomial of degree `k-1`, the task is to compute the **constant term (`c`)** using **Lagrange Interpolation**.

Each input point is encoded with:
- A key `x` (integer)
- A value `y`, encoded in a specified number base

---

## 🧩 How It Works

1. **Read input** from JSON files (`testcase1.json`, `testcase2.json`)
2. **Decode the values** using the specified base
3. **Apply Lagrange Interpolation** to find the value of the polynomial at `x = 0` (i.e. the secret)

---

## 🔧 How to Run

### 🖥 Requirements

- Java 11 or higher
- A terminal/command line interface

### ▶️ Steps

```bash
javac SecretSharing.java
java SecretSharing
```
