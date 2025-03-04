import axios from 'axios'

export default {
  async register(user) {
    if (!user) {
      throw new Error('Please fill in all the required fields!')
    }

    for (const property in user) {
      if (!user[property]) {
        throw new Error(`${property} is required!`)
      }
    }

    const response = await axios.post('http://localhost:8080/users/register', user)

    if (response.status !== 200) {
      throw new Error(response.statusText)
    }

    return response
  },
}
