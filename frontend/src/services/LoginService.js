export default {
  async login(login, router) {
    console.log(login)
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(login),
    })
      .then((response) => {
        if (response.status === 200) {
          router.push('/appointments')
        }
      })
      .catch((error) => {
        console.error('Fehler beim weiterleiten:', error)
      })
  },
}
