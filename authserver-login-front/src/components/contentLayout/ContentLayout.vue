<template>
  <div class="content-layout"  ref="body" :class="{backtop:backtopFlag}">
    <!-- <div class="content-layout__head" v-if="showTitle" :style="{ width: width ? width+ 'px' : 100+'%' }">
      <div class="content-layout__title">
        <slot name="title"></slot>
      </div>
      <div class="content-layout__right">
        <slot name="right"></slot>
      </div>
    </div> -->
    <el-page-header
      class="content-layout__head"
      @back="goBack"
      content=""
      v-if="showTitle"
      :style="{ width: width ? width + 'px' : 100 + '%' }"
    >
      <span slot="content"><slot name="title"></slot></span>
    </el-page-header>
    <div
      class="content-layout__body"
      :style="{ paddingTop: showTitle ? 80 + 'px' : 24 + 'px' }"
    >
      <slot name="body"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ContentLayout',
  props: {
    showTitle: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      searchStr: '',
      width: '',
      backtopFlag:false
    }
  },
  created () {
    window.addEventListener('resize', this.getHeight)
    this.getHeight() 
  },
  destroyed () {
    window.removeEventListener('resize', this.getHeight)
  }, 
  methods: {
    handleSearch () {
      this.$emit('refreshList', this.searchStr)
    },
    getHeight () {
      this.backtopFlag = true
      setTimeout(()=>{
          this.backtopFlag = false
       },200)
      this.$nextTick(() => { 
        this.width = this.$refs.body.clientWidth || '' 
      })
    }
  }
}
</script>

<style lang='scss' scoped>
.backtop{ 
    overflow: hidden;
    width: 100%;
    height: 80vh;
}
.helpCenter {
  color: #1ba1ba;
  text-decoration: none;
  font-size: 15px;
}
.content-layout {
  background: #fff;
  &__head {
    // box-sizing: border-box;
    // border-bottom: 1px solid #ddd;
    // display: flex;
    // flex-flow: row nowrap;
    // justify-content: space-between;
    // padding: 0px 10px;
    // width: 100%;
    // border-top: 1px solid #e5e5e5;
    line-height: 59px;
    height: 59px;
    background: #fff;
    position: fixed;
    z-index: 10;
  }
  // &__title {
  //   display: inline-block;
  //   font-size: 18px;
  //   font-weight: 400;
  //   margin-left: 16px;
  // }
  // &__right {
  // }
  &__body {
    padding: 20px 20px;
    padding-top: 80px;
  }
}
/*滚动条样式*/
.content-layout__body::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 6px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 3px;
  cursor: pointer;
}
.content-layout__body::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 6px;
  -webkit-box-shadow: inset 0 0 2px rgba(66, 36, 36, 0.2);
  background: rgba(0, 0, 0, 0.1);
  cursor: pointer;
}
.content-layout__body::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  /* -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2); */
  background-color: transparent;
  border-radius: 0;
  /* background: rgba(0, 0, 0, 0.1); */
}
</style>
