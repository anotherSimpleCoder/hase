<template>
  <header>
    <div>
      <nav>
        <RouterLink to="/">Home</RouterLink> | <RouterLink to="/about">About</RouterLink> |
        <RouterLink to="/appointments">Appointents</RouterLink>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<style scoped></style>
