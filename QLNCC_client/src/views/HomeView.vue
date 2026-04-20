<script setup lang="ts">
import { ref } from 'vue'
import ExampleForm from '@/components/ExampleForm.vue'
import { submitExam } from '@/api/exam'

const message = ref('')
const error = ref('')
const sending = ref(false)

async function onExamSubmit(payload: { title: string; note: string }) {
  message.value = ''
  error.value = ''
  sending.value = true
  try {
    await submitExam(payload)
    message.value = 'Đã gọi BE thành công — xem log ExamController (không lưu DB).'
  } catch (e) {
    error.value =
      e instanceof Error ? e.message : 'Không gọi được BE (CORS / mạng / URL).'
  } finally {
    sending.value = false
  }
}
</script>

<template>
  <section class="mx-auto max-w-lg rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
    <h1 class="mb-1 text-xl font-semibold text-slate-900">Trang chủ</h1>
    <p class="mb-6 text-sm text-slate-600">
      Gửi form gọi <code class="rounded bg-slate-100 px-1">submitExam()</code> trong
      <code class="rounded bg-slate-100 px-1">src/api/exam.ts</code> → POST
      <code class="rounded bg-slate-100 px-1">/api/v1/exam/submit</code>.
    </p>
    <ExampleForm :sending="sending" @submit="onExamSubmit" />
    <p v-if="sending" class="mt-4 text-sm text-slate-500">Đang gửi…</p>
    <p v-else-if="message" class="mt-4 text-sm font-medium text-emerald-700">{{ message }}</p>
    <p v-else-if="error" class="mt-4 text-sm font-medium text-red-600">{{ error }}</p>
  </section>
</template>
