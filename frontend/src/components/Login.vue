<template>
  <div class="container">
    <h1>HASE</h1>
    <p>Please enter your login data</p>

    <div class="input-group">
      <input v-model="newLogin.email" type="text" placeholder="Enter your email" />
      <input v-model="newLogin.password" type="password" placeholder="Password" />
    </div>

    <p class="signup-text">
      Do not have an account? <router-link to="/register">Sign up here!</router-link>
    </p>

    <button @click="login(newLogin)">Login</button>

    <footer>
      HASE is a project made by <span class="blue">H</span>anan, <span class="blue">A</span>msakan,
      <span class="blue">S</span>ophia and <span class="blue">E</span>rwin. This program can be used
      to book and regulate appointments!
    </footer>
  </div>
</template>

<script>
import LoginService from '@/services/LoginService'

export default {
  data() {
    return {
      newLogin: {
        email: undefined,
        password: undefined,
      },
    }
  },
  methods: {
    login(newLogin) {
      console.log(newLogin)
      LoginService.login(newLogin, this.$router)
    },
  },
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  min-height: 100vh;
  background-color: white;
}

h1 {
  font-size: 8rem;
  color: rgb(51, 97, 148);
  margin-bottom: 10px;
}

p {
  font-size: 1.5rem;
  color: black;
  margin: 10px 0;
}

.input-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  margin: 20px 0;
}

input {
  padding: 10px;
  font-size: 1rem;
  width: 250px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.signup-text {
  font-size: 1rem;
}

.signup-text a {
  color: blue;
  text-decoration: none;
}

.signup-text a:hover {
  text-decoration: underline;
}

button {
  font-size: 1.2rem;
  padding: 10px 20px;
  background-color: rgb(51, 97, 148);
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

button:hover {
  background-color: rgb(40, 80, 120);
}

footer {
  font-size: 1rem;
  margin-top: 20px;
  color: gray;
}

.blue {
  color: blue;
  font-size: 1.25rem;
  font-weight: bolder;
}
</style>
