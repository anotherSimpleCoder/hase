export default {
  async login(login) {
    if(!login) {
      throw new Error("Please fill in all the required fields!")
    }

    for(const property in login) {
      if(!login[property]) {
        throw new Error(`${property} is required`)
      }
    }

    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(login),
    })

    if (response.status !== 200) {
      var errorResponse = await response.json()
      throw new Error(errorResponse.message)
    }

    return await response.json()
  },

  async getUser(email) {
    if(!email) {
      throw new Error('Invalid request: email is missing!')
    }

    try {
      const response = await fetch(`http://localhost:8080/auth/login?email=${email}`)
      return await response.json()
    } catch (error) {
      throw new Error('Error handling data:', error)
    }
  },
}
