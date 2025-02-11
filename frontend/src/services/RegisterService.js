import axios from 'axios'

export default {
  async register(user) {


    const response = await axios.post('http://localhost:8080/users', user)

    if (response.status !== 200) {
      throw new Error(response.statusText)
    }

    return response
  },
}
