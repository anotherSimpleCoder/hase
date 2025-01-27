<template>
  <header>
    <div>
      <nav>
        <RouterLink to="/users">User</RouterLink> |
        <RouterLink to="/appointments">Appointents</RouterLink>
        <div>User:{{ user }}</div>
      </nav>
    </div>
  </header>
  <button v-if="!user" @click="login">Anmelden</button>
  <button v-else @click="logout">Abmelden</button>

  <RouterView />
</template>

<script>
import { RouterLink, RouterView } from 'vue-router'
import auth from './auth'

export default {
  name: 'App',
  components: {
    RouterLink,
    RouterView,
  },
  data() {
    return {
      user: null,
    }
  },
  methods: {
    login() {
      auth.login()
      console.log(user)
    },
    logout() {
      auth.logout()
    },
  },
  mounted() {
    auth.getUser().then((user) => {
      this.user = user
    })
  },
}
</script>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
  display: flex;
  justify-content: center;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 2vw;
  text-align: center;
  margin-top: 2rem;
  color: black;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  transform: translateY(-30%);
}
nav a {
  text-decoration: none;
  color: black;
}

nav a.router-link-exact-active {
  color: rgb(64, 255, 124);
}

nav a:hover {
  background-color: transparent;
  transform: scale(110%);
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
}
</style>
