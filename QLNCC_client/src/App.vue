<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="min-h-screen bg-slate-50 text-slate-900">
    <header class="border-b border-slate-200 bg-white">
      <div class="mx-auto flex max-w-3xl items-center gap-6 px-4 py-3">
        <span class="font-semibold text-emerald-800">QLNCC</span>
        <nav class="flex gap-4 text-sm">
          <RouterLink
            to="/"
            class="text-slate-600 hover:text-emerald-700"
            active-class="font-medium text-emerald-700"
          >
            Trang chủ
          </RouterLink>
        </nav>
      </div>
    </header>
    <main class="mx-auto max-w-3xl px-4 py-8">
      <RouterView />
    </main>
  </div>
</template>
