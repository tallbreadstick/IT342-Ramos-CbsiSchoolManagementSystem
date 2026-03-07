import axios from 'axios'
import { BASE_DIR } from './server'

const api = axios.create({
  baseURL: BASE_DIR,
  // include credentials if your backend needs cookies; adjust as required
  withCredentials: false,
})

export default api
