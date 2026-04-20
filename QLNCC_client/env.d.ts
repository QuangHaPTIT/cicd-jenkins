/// <reference types="vite/client" />

declare const __VITE_API_BASE_URL__: string

interface ImportMetaEnv {
  readonly VITE_API_BASE_URL?: string
  readonly VITE_DEV_PROXY_TARGET?: string
}
