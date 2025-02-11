<template>
  <div>Verarbeite Anmeldung...</div>
</template>

<script>
import auth from '@/auth'

export default {
  name: 'CallbackComponent',
  mounted() {
    auth.handleGoogleCallback().then(() => {
      this.$router.push('/')
    })
  },
}
</script>
