<template>
  <header>
    <div>
      <nav>
        <RouterLink to="/">your Appointments</RouterLink> |
        <RouterLink to="/appointments"> All Appointments</RouterLink>
      </nav>
    </div>
    <div class="account-button"><button @click="togglePopup()">Account</button></div>
    <div class="popup-overlay" v-if="popupVisible" @click="togglePopup()">
      <div class="popup-content" @click.stop>
        <div v-if="userStore.user">
          Name: {{ userStore.user.firstName }} {{ userStore.user.lastName }}
        </div>
        <div v-if="userStore.user">email: {{ userStore.user.email }}</div>
        <div v-else>Please log in</div>
        <button @click="(login(), togglePopup())">Login</button>
        <button @click="(logout(), togglePopup())">logout</button>
      </div>
    </div>
  </header>
</template>

<script>
import { RouterLink, RouterView } from 'vue-router'
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
      popupVisible: false,
    }
  },
  methods: {
    logout() {
      this.userStore.logoutUser()
    },
    login() {
      this.$router.push('/login')
    },
    togglePopup() {
      this.popupVisible = !this.popupVisible
    },
  },
}
</script>

<style scoped>
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.popup-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
}

header {
  line-height: 1.5;
  max-height: 100vh;
  display: flex;
  margin-left: -20px;
  margin-top: -20px;
  margin-right: -20px;
  justify-content: center;
  background-color: rgb(51, 97, 148);
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  display: flex;
  width: 100%;
  font-size: 1vw;
  text-align: center;
  color: black;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}
nav a {
  display: flex;
  width: 100%;
  text-decoration: none;
  color: white;
  font-size: 150%;
}

nav a.router-link-exact-active {
  color: rgb(0, 202, 50);
}

nav a:hover {
  background-color: transparent;
  color: rgb(0, 202, 50);
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }

  .account-button {
    position: absolute;
    top: 0;
    right: 10px;
    padding: 1 em;
  }
}
</style>
