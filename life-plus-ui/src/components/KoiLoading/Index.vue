<template>
  <div>
    <transition name="el-fade-in-linear">
      <div class="loading-box" v-if="props.loading">
        <div class="loading">
          <span :style="`--i: ${i}`" v-for="i in 7"></span>
        </div>
        <svg>
          <filter id="gooey">
            <feGaussianBlur in="SourceGraphic" stdDeviation="10" />
            <feColorMatrix
              values="1 0 0 0 0 
                        0 1 0 0 0
                        0 0 1 0 0 
                        0 0 0 20 -10"
            />
          </filter>
        </svg>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
interface ILoadingProps {
  loading?: boolean;
}

const props = withDefaults(defineProps<ILoadingProps>(), {
  loading: false
});
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
  background: rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.18);
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

  &.out-circle {
    animation: circle-out-center 2.5s cubic-bezier(0.25, 1, 0.3, 1);
  }

  $circle-center-center-in: circle(125% at 50% calc(50vh - 150px + 28px));
  $circle-center-center-out: circle(0% at 50% calc(50vh - 150px + 28px));

  @keyframes circle-out-center {
    0% {
      clip-path: var($circle-center-center-in);
    }
    100% {
      clip-path: var($circle-center-center-out);
    }
  }
}
</style>
