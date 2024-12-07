<template>
  <div>
    <transition name="el-fade-in-linear">
      <div class="loading-box" :transition-style="loading ? '' : 'out:circle:center'">
        <div class="loading">
          <span :style="`--i: ${i}`" v-for="i in 7"></span>
        </div>

        <svg>
          <filter id="gooey">
            <feGaussianBlur in="SourceGraphic" stdDeviation="10" />
            <!-- feColorMatrix 用于彩色滤光片转换 -->
            <feColorMatrix
              values="
            1 0 0 0 0 
            0 1 0 0 0
            0 0 1 0 0 
            0 0 0 20 -10
            "
            />
          </filter>
        </svg>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";

const loading = ref(true);

onMounted(() => {
  // 取消加载
  cancelLoading();
});

const cancelLoading = () => {
  setTimeout(() => {
    loading.value = false
  }, 1500)
};
</script>

<style lang="scss" scoped>
.loading-box {
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.2);
  // animation-name: circle-out-center;
  animation-duration: 2.5s;
  animation-timing-function: cubic-bezier(0.25, 1, 0.3, 1);
  animation-delay: 0;
  will-change: clip-path;
  animation-fill-mode: both;

  svg {
    width: 0;
    height: 0;
  }

  .loading {
    position: relative;
    width: 300px;
    height: 300px;
    filter: url("#gooey");
  }

  .loading span {
    position: absolute;
    top: 0;
    left: 0;
    display: block;
    width: 100%;
    height: 100%;
    animation: loading 3s ease-in-out infinite;

    /* var函数用来插入css变量的值，css变量名称以--开头 */
    animation-delay: calc(0.2s * var(--i));
  }

  .loading span::before {
    position: absolute;
    top: 0;
    left: calc(50% - 28px);
    width: 56px;
    height: 56px;
    content: "";
    background-image: linear-gradient(to right, #92fe9d 0%, #00c9ff 100%);
    border-radius: 50%;
    box-shadow: 0 0 30px #00c9ff;
  }

  @keyframes loading {
    0% {
      transform: rotate(0deg);
    }

    50%,
    100% {
      transform: rotate(360deg);
    }
  }

  @keyframes circle-out-center {
    0% {
      clip-path: circle(125% at 50% calc(50vh - 150px + 28px));
    }

    100% {
      clip-path: circle(0% at 50% calc(50vh - 150px + 28px));
    }
  }

  &[transition-style="out:circle:center"] {
    animation-name: circle-out-center;
  }
}
</style>
