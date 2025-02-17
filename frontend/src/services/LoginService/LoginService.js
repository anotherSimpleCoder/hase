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

  async getUser(login) {
    console.log(login)
    try {
      const response = await fetch(`http://localhost:8080/auth/login?email=${login}`)
      const data = await response.json()
      console.log(data)
      return data
    } catch (error) {
      console.error('Fehler beim bearbeiten der Daten:', error)
    }
  },
}
