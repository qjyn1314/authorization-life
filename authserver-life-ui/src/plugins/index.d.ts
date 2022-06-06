import { AxiosInstance } from 'axios';

declare global {
  interface Window {
    axios: AxiosInstance
  }
}

declare module 'vue/types/vue' {
  interface Vue {
    $axios: AxiosInstance
  }
  interface VueConstructor {
    $axios: AxiosInstance
  }
}
