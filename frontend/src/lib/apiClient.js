import axios from 'axios'

// 45s timeout covers Render free-tier cold starts (typically 20-30s).
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api/v1',
  timeout: 45_000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export default apiClient
