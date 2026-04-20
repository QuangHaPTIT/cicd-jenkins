import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const raw = typeof env.VITE_API_BASE_URL === 'string' ? env.VITE_API_BASE_URL.trim() : ''
  const apiBase = raw.replace(/\/$/, '')
  const devProxyTargetRaw =
    typeof env.VITE_DEV_PROXY_TARGET === 'string' ? env.VITE_DEV_PROXY_TARGET.trim() : ''
  const devProxyTarget = (devProxyTargetRaw).replace(/\/$/, '')

  return {
    define: {
      __VITE_API_BASE_URL__: JSON.stringify(apiBase),
    },
    plugins: [
      vue(),
      vueDevTools(),
      tailwindcss(),
    ],
    server: {
      proxy: {
        '/api': {
          target: devProxyTarget,
          changeOrigin: true,
        },
      },
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
  }
})
