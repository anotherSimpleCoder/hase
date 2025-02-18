<template>
  <div class="container">
    <div v-if="success">
      <p id="success-text">You have been registered successfully!</p>
      <p>Please login <router-link to="/login">here</router-link></p>
    </div>

    <div v-if="error.flag">
      <p id="error-text">Error: {{error.message}}</p>
    </div>

    <img src="/logo.svg" height="250" width="250" alt="logo">

    <h1>HASE</h1>
    <p>Please enter your registration data!</p>

    <div class="input-group">
      <input type="text" placeholder="Roll number" v-model="newUser.matrikelNr" @keydown="handleKeydown"/>
      <input type="text" placeholder="First Name" v-model="newUser.firstName" @keydown="handleKeydown"/>
      <input type="text" placeholder="Last name" v-model="newUser.lastName" @keydown="handleKeydown"/>
      <input type="text" placeholder="Email" v-model="newUser.email" @keydown="handleKeydown"/>
      <input type="password" placeholder="password" v-model="newUser.password" @keydown="handleKeydown"/>

      <p class="signin-text">
        Already have an account? <router-link to="/login">Log in here!</router-link>
      </p>

      <button @click="addUser(this.newUser)">Register</button>
    </div>
  </div>
</template>

<script>
import RegisterService from '@/services/RegisterService/RegisterService.js'

export default {
  setup() {
    return {
      RegisterService
    }
  },
  data() {
    return {
      newUser: {
        matrikelNr: '',
        firstName: '',
        lastName: '',
        email: '',
        password: '',
      },

      success: false,
      error: {
        flag: false,
        message: '',
      }
    }
  },

  methods: {
    addUser(user) {
      this.error.flag = false;
      this.success = false;

      RegisterService.register(user)
        .then(() => (this.success = true))
        .catch((error) => {
          this.error = {
            flag: true,
            message: error.message,
          }
        })
    },

    handleKeydown(keyEvent) {
      if(keyEvent.key === 'Enter') {
        this.addUser(this.newUser)
      }
    }
  },
}
</script>

<style scoped lang="scss">
img {
  margin-top: 2rem;
}

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
  margin-top: 0;
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

.signin-text {
  font-size: 1rem;

  a {
    color: blue;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
}

button {
  font-size: 1rem;
  padding: 10px 50px;
  background-color: rgb(51, 97, 148);
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;

  &:hover {
    background-color: rgb(40, 80, 120);
  }
}

#success-text {
  color: green;
}

#error-text {
  color: red;
}
</style>
