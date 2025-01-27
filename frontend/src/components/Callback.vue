<template>
  <div>Verarbeite Anmeldung...</div>
</template>

<script>
import auth from '@/auth'

export default {
  name: 'Callback',
  mounted() {
    auth.handleCallback().then(() => {
      this.$router.push('/')
    })
  },
}
</script>
