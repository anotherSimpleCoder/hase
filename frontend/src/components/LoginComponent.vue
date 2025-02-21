<template>
  <div class="container">
    <div v-if="success">
      <p id="success-text">You have logged in successfully!</p>
    </div>

    <div v-if="error.flag">
      <p id="error-text">Error: {{ error.message }}</p>
    </div>

    <img src="/logo.svg" height="250" width="250" alt="logo">

    <h1>HASE</h1>
    <p>Please enter your login data</p>

    <div class="input-group">
      <input
        v-model="newLogin.email"
        type="text"
        placeholder="Enter your email"
        @keydown="handleKeydown"
      />
      <input
        v-model="newLogin.password"
        type="password"
        placeholder="Password"
        @keydown="handleKeydown"
      />
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
import AuthService from '@/services/AuthService/AuthService.js'


export default {
  setup() {
    return {}
  },
  data() {
    return {
      newLogin: {
        email: undefined,
        password: undefined,
      },
      user: undefined,

      success: false,
      error: {
        flag: false,
        message: '',
      },
    }
  },
  methods: {
    async login(newLogin) {
      this.success = false
      this.error.flag = false
      AuthService.login(newLogin, this.$router)
        .then(async () => {
          this.success = true
          this.$router.push('/my-appointments')
        })
        .catch((error) => {
          this.error = {
            flag: true,
            message: error.message,
          }
        })
    },

    async handleKeydown(keyEvent) {
      if (keyEvent.key === 'Enter') {
        await this.login(this.newLogin)
      }
    },
  },
  async created() {
    if (AuthService.isLoggedIn()) {
      const loggedInUser = await AuthService.getMe()

      alert(
        `You are already logged in as ${loggedInUser.firstName} ${loggedInUser.lastName}`,
      )
      this.$router.push('/my-appointments')
    }
  },
}
</script>

<style scoped lang="scss">
img {
  margin-top: 2rem;
}

body {
  background-color: white;
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
  font-size: 8rem;
  color: rgb(51, 97, 148);
  margin-bottom: 10px;
  margin-top: 0;
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

  a {
    color: blue;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
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

  &:hover {
    background-color: rgb(40, 80, 120);
  }
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
