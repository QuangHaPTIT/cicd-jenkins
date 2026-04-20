/** Base URL BE — giá trị inject từ `vite.config.ts` (`define` + `loadEnv`). */
export function getApiBaseUrl(): string {
  return __VITE_API_BASE_URL__
}
