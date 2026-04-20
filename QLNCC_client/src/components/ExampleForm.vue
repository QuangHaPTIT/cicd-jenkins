<script setup lang="ts">
import { reactive } from 'vue'

defineProps<{
  sending?: boolean
}>()

const emit = defineEmits<{
  submit: [payload: { title: string; note: string }]
}>()

const form = reactive({
  title: '',
  note: '',
})

function onSubmit() {
  emit('submit', { title: form.title, note: form.note })
}
</script>

<template>
  <form class="flex flex-col gap-4" @submit.prevent="onSubmit">
    <div>
      <label for="title" class="mb-1 block text-sm font-medium text-slate-700">Tiêu đề</label>
      <input
        id="title"
        v-model="form.title"
        type="text"
        name="title"
        class="w-full rounded-md border border-slate-300 px-3 py-2 text-slate-900 shadow-sm focus:border-emerald-500 focus:outline-none focus:ring-1 focus:ring-emerald-500"
        placeholder="Nhập tiêu đề"
        autocomplete="off"
      />
    </div>
    <div>
      <label for="note" class="mb-1 block text-sm font-medium text-slate-700">Ghi chú</label>
      <textarea
        id="note"
        v-model="form.note"
        name="note"
        rows="3"
        class="w-full rounded-md border border-slate-300 px-3 py-2 text-slate-900 shadow-sm focus:border-emerald-500 focus:outline-none focus:ring-1 focus:ring-emerald-500"
        placeholder="Nhập ghi chú"
      />
    </div>
    <button
      type="submit"
      :disabled="sending"
      class="inline-flex w-fit items-center rounded-md bg-emerald-600 px-4 py-2 text-sm font-medium text-white hover:bg-emerald-700 disabled:cursor-not-allowed disabled:opacity-60"
    >
      {{ sending ? 'Đang gửi…' : 'Gửi' }}
    </button>
  </form>
</template>
