# 🧠 AI Interview Preparation Portal

A backend, AI-powered web application that helps users prepare for technical interviews with **MCQs**, **mock interviews**, **resume feedback**, and **personalized AI guidance**.

---

## 🚀 Features

### 👨‍🎓 For Users
- 🔐 **Register/Login** with JWT authentication
- 🎯 **Topic-based Quiz** system (Java, C, DSA, etc.)
- 📊 **MCQ Feedback** with score tracking
- 🧠 **AI Mock Interviews** with real-time answer evaluation
- 📁 **Resume Analyzer**: Upload resume + JD and get detailed feedback
- 📚 **AI FAQs**: Frequently asked questions with sample answers
- 📈 **Quiz History**: Track all your previous attempts and performance

### 🛠️ For Admins
- 👥 View all registered users
- ❌ Remove users and clean up associated data

---

## ⚙️ Tech Stack

### 💻 Backend
- **Spring Boot**
- **Spring Security + JWT**
- **MongoDB (Atlas)**
- **Apache Tika** (for resume text extraction)
- **Spring AI + OpenRouter API** with `deepseek` model

### 🌐 Frontend
- React (📌 Sample frontend code available separately — plug & play)

---

## 🧩 AI Integration

All AI tasks are powered by [`OpenRouter`](https://openrouter.ai/) using the model:

- [`deepseek/deepseek-r1-distill-llama-70b`](https://openrouter.ai/models/deepseek/deepseek-r1-distill-llama-70b)

Used for:
- Question generation
- Answer evaluation
- Resume analysis
- FAQ suggestions

---

## 📁 Project Structure (Backend)

src/
- ├── controller/ # All REST APIs (Auth, Mock, Admin, Resume, FAQ)
- ├── model/ # MongoDB document models and DTOs
- ├── repository/ # Spring Data MongoDB Repos
- ├── service/ # Core business logic and AI integration
- └── config/ # Spring Security and JWT config (if added)

---

## 🧪 Example AI Prompt Use

**Mock Interview Generator:**
- Generate easy level interview questions on DSA. Only list of 2 questions, no extra line of text, no answers.

**Answer Evaluation:**
- Evaluate this answer for the question... Score out of 10. Suggest improvements.

**Resume Analyzer:**
- Analyze the resume and job description, then give professional feedback in bullet points.

  ---

## 🔐 Authentication & Authorization

- Uses **Spring Security** with **JWT**
- Role-based access (`USER`, `ADMIN`)
- Tokens include roles and email
- Frontend should store the token and send it in headers

---

## 🛠️ Setup & Run Locally

### 1️⃣ Prerequisites
- Java 17+
- Maven
- MongoDB Atlas (or local MongoDB)
- OpenRouter API Key

### 2️⃣ Clone the Repo

```bash
git clone https://github.com/your-username/ai-interview-portal.git
cd ai-interview-portal
``````

### 3️⃣ Configure application.properties ###

- spring.data.mongodb.uri=your_mongo_uri
- spring.data.mongodb.database=placment

- jwt.secret=your_jwt_secret
- jwt.expiration=3600000

- spring.ai.openai.api-key=your_openrouter_api_key
- spring.ai.openai.base-url=https://openrouter.ai/api
- spring.ai.openai.model=deepseek/deepseek-r1-distill-llama-70b


### 4️⃣ Run the Application  ###
- ./mvnw spring-boot:run

## 🧪 Postman Collection  ##

You can import the Postman collection here to test all endpoints like:

- /api/auth/register
- /api/auth/login
- /api/mock/generate
- /api/mock/evaluate
- /api/resume/analyze
- /api/admin/dashboard/users

