<template>
  <header>
    <div class="top-container">
      <nav>
        <RouterLink to="/my-appointments" v-if="authService.isLoggedIn()"
          >My Appointments</RouterLink
        >
        <RouterLink to="/appointments" v-if="authService.isLoggedIn()">All Appointments</RouterLink>
        <RouterLink to="/login" v-if="!authService.isLoggedIn()">Login</RouterLink>
        <RouterLink to="/register" v-if="!authService.isLoggedIn()">Register</RouterLink>
        <RouterLink to="/calender" v-if="authService.isLoggedIn()">Calendar</RouterLink>
      </nav>

    <!-- 
      <div class="account-button" v-if="authService.isLoggedIn()">
      <button @click="togglePopup()">Account</button>
    </div> -->
<button v-if="authService.isLoggedIn()" class="account-button" @click="togglePopup()">Account</button>


    </div>

    <div class="popup-overlay" v-if="popupVisible" @click="togglePopup()">
      <div v-if="loadingUser">Loading...</div>

      <div class="popup-content" v-if="!loadingUser" @click.stop>
        <h3 class="popup-text">Personal Information</h3>
        <div v-if="loggedInUser">
          Name: {{ loggedInUser.firstName }} {{ loggedInUser.lastName }}
        </div>
        <div  v-if="loggedInUser">Email: {{ loggedInUser.email }}</div>
        <div v-else>Please log in</div>
       <!--
        <button @click="(login(), togglePopup())">Login</button>
      -->
        <button @click="(logout(), togglePopup())">Logout</button>
      </div>
    </div>
  </header>
</template>

<script>
import AuthService from '@/services/AuthService/AuthService.js'

export default {
  setup() {
    return {
      authService: AuthService,
      loadingUser: false,
      loggedInUser: null,
    }
  },
  data() {
    return {
      popupVisible: false,
    }
  },
  async beforeCreate() {
    if (AuthService.isLoggedIn()) {
      this.loadingUser = true
      AuthService.getMe()
        .then((user) => {
          this.loggedInUser = user
          this.loadingUser = false
        })
        .catch((error) => {
          console.error(error)
          this.loadingUser = false
        })
    }
  },
  methods: {
    logout() {
      AuthService.logout()
      this.$router.push('/login')
    },
    login() {
      this.$router.push('/login')
    },
    togglePopup() {
      AuthService.getMe().then((user) => {
        this.loggedInUser = user
      })

      this.popupVisible = !this.popupVisible
    },
  },
}
</script>

<style scoped lang="scss">
.top-container{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: rgb(51, 97, 148);
}

.account-button {
  justify-content: center;
  align-items: center;
  padding: 1rem 2rem;
  background-color: rgb(51, 97, 148);
  color: white;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  outline: none;
  font-weight: bold;
}
.account-button:hover {
  background-color: transparent;
  color: rgb(0, 202, 50);

}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.3s ease-out;
  z-index: 9999;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.popup-content {
  background: white;
  padding: 25px;
  border-radius: 12px;
  width: 350px; 
  max-width: 90%; 
  animation: slideUp 0.3s ease-out;
  text-align: center;
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.popup-text {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: black;
  margin-bottom: 20px;
  margin-top: 2px;
}

.popup-content button {
  background-color: rgb(51, 97, 148);
  color: white;
  font-size: 1rem;
  padding: 12px 25px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  width: 100%;
  margin-top: 20px;
  margin-bottom: 5px;
  transition: background-color 0.3s ease;
}

.popup-content button:hover {
  background-color: rgb(0, 202, 50);
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

nav {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  gap: 20px;
  color: black;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;

  a {
    width: 100%;
    text-decoration: none;
    color: white;
    font-size: 20px;

    display: inline-block;
    padding: 0 1rem;
    border-left: 1px solid var(--color-border);

    .router-link-exact-active {
      color: rgb(0, 202, 50);
    }

    &:hover {
      background-color: transparent;
      color: rgb(0, 202, 50);
    }

    &:first-of-type {
      border: 0;
    }
  }
  a.router-link-exact-active {
    color: rgb(0, 202, 50);
  }
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  header {
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
    position: absolute ;
    top: 15;
    right: 10px;
    padding: 1em;
  }
}
</style>
