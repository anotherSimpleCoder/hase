<template>
  <div class="container">
    <h1>HASE</h1>
    <p>Please enter your login data</p>

    <div class="input-group">
      <input v-model="newLogin.email" type="text" placeholder="Enter your email" @keydown="handleKeydown()"/>
      <input v-model="newLogin.password" type="password" placeholder="Password" @keydown="handleKeydown()"/>
    </div>

    <p class="signup-text">
      Do not have an account? <router-link to="/register">Sign up here!</router-link>
    </p>

    <button @click="login(newLogin)">Login</button>
    <div v-if="user">{{ user.firstName }}</div>
    <div>{{ userStore.user }}</div>

    <footer>
      HASE is a project made by <span class="blue">H</span>anan, <span class="blue">A</span>msakan,
      <span class="blue">S</span>ophia and <span class="blue">E</span>rwin. This program can be used
      to book and regulate appointments!
    </footer>
  </div>
</template>

<script>
import LoginService from '@/services/LoginService'
import { useUserStore } from './stores/userStore'

export default {
  setup() {
    const userStore = useUserStore()

    return {
      userStore,
    }
  },
  data() {
    return {
      newLogin: {
        email: undefined,
        password: undefined,
      },
      user: undefined,
    }
  },
  methods: {
    async login(newLogin) {
      console.log(newLogin)
      const userData = await LoginService.getUser(newLogin.email)
      this.user = userData[0]
      console.log(this.user)

      this.userStore.setUser(this.user)

      await LoginService.login(newLogin, this.$router)
    },

    handleKeydown(keyEvent) {
      console.log(keyEvent.key)
    },
  },
  created() {
    if (this.userStore.user) {
      alert(
        `You are already logged in as ${this.userStore.user.firstName} ${this.userStore.user.lastName}`,
      )
      this.$router.push('/appointments')
    }
  },
}
</script>

<style scoped lang="scss">
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
