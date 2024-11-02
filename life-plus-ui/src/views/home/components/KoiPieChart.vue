<template>
  <div ref="refChart" style="width: 100%; height: 350px"></div>
</template>

<script setup lang="ts">
import * as echarts from "echarts";
import { ref, onMounted, onUnmounted } from "vue";

const refChart = ref();
const chartInstance = ref();
// 接口数据
const allData = ref([
  { value: 5, name: "AABB故障" },
  { value: 6, name: "CCDD故障" },
  { value: 7, name: "TTZZ故障" },
  { value: 8, name: "GGHH故障" },
  { value: 9, name: "YYXX故障" }
]);

// tootip定时器
const tootipTimer = ref();

onMounted(() => {
  // 图表初始化
  initChart();
  // 获取接口数据
  getData();
  // 调用Echarts图表自适应方法
  screenAdapter();
  // Echarts图表自适应
  window.addEventListener("resize", screenAdapter);
  // Tootip刷新定时器
  getTootipTimer();
});

onUnmounted(() => {
  // 销毁Echarts图表
  chartInstance.value.dispose();
  chartInstance.value = null;
  // 清除局部刷新定时器
  clearInterval(tootipTimer.value);
  tootipTimer.value = null;
  // Echarts图表自适应销毁
  window.removeEventListener("resize", screenAdapter);
});

const initChart = () => {
  chartInstance.value = echarts?.init(refChart.value);
  const initOption = {
    tooltip: {
      confine: true,
      trigger: "item"
    },
    legend: {
      orient: "vertical",
      left: "left",
      extraCssText: "z-index: 999"
    },
    series: [
      {
        name: "模块故障",
        type: "pie",
        // 环形图大小
        radius: ["45%", "70%"],
        // 环形图位置
        center: ["60%", "50%"],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: "#fff",
          borderWidth: 2
        },
        label: {
          show: false,
          position: "center",
          formatter: "{d}%" // 当前百分比
        },
        emphasis: {
          label: {
            show: true,
            fontSize: "16",
            fontWeight: "bold"
          }
        },
        labelLine: {
          show: false
        }
      }
    ]
  };
  // 图表初始化配置
  chartInstance.value?.setOption(initOption);

  // 鼠标移入停止定时器
  chartInstance.value.on("mouseover", () => {
    clearInterval(tootipTimer.value);
  });

  // 鼠标移入启动定时器
  chartInstance.value.on("mouseout", () => {
    getTootipTimer();
  });
};

const getData = () => {
  // 调用接口方法
  // getData().then(res => {
  //   xChartData.value = res.data;
  //   updateChart();
  //   // echarts查不到数据，将初始化echarts的方法全部放置到接口方法中即可。
  // })
  // 获取服务器的数据, 对xChartData进行赋值之后, 调用updateChart方法更新图表
  updateChart();
};

const updateChart = () => {
  // 处理图表需要的数据
  const dataOption = {
    series: [
      {
        data: allData.value
      }
    ]
  };
  // 图表数据变化配置
  chartInstance.value?.setOption(dataOption);
};

const screenAdapter = () => {
  const adapterOption = {
    // 圆点分类标题
    legend: {
      textStyle: {
        fontSize: 12
      }
    }
  };
  // 图表自适应变化配置
  chartInstance.value?.setOption(adapterOption);
  // 手动的调用图表对象的resize 才能产生效果
  chartInstance.value?.resize();
};

// 定时器
const getTootipTimer = () => {
  let index = 0; // 播放所在下标
  tootipTimer.value = setInterval(() => {
    // echarts实现定时播放tooltip
    chartInstance.value.dispatchAction({
      type: "showTip",
      position: function (point: any) {
        return {
          left: point[0] + 10, // 水平方向偏移量
          top: point[1] - 10 // 垂直方向偏移量
        };
      },
      seriesIndex: 0,
      dataIndex: index
    });
    index++;
    if (index > allData.value.length) {
      index = 0;
    }
  }, 2000);
};
</script>

<style scoped></style>
