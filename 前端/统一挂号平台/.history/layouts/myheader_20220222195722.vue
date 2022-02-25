<template>
  <div class="header-container">
    <div class="wrapper">
      <!-- logo -->
      <div class="left-wrapper v-link selected">
        <img
          style="width: 50px"
          width="50"
          height="50"
          src="~assets/images/logo.png"
        />
        <span class="text">尚医通 预约挂号统一平台</span>
      </div>
      <!-- 搜索框 -->
      <div class="search-wrapper">
        <div class="hospital-search animation-show">
          <el-autocomplete
            class="search-input small"
            prefix-icon="el-icon-search"
            v-model="hosname"
            :fetch-suggestions="querySearchAsync"
            placeholder="点击输入医院名称"
            @select="handleSelect"
          >
            <span
              slot="suffix"
              class="search-btn v-link highlight clickable selected"
              >搜索
            </span>
          </el-autocomplete>
        </div>
      </div>
      <!-- 右侧 -->
      <div class="right-wrapper">
        <span class="v-link clickable">帮助中心</span>
        <!--        <el-dropdown >-->
        <!--              <span class="el-dropdown-link">-->
        <!--                晴天<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
        <!--              </span>-->
        <!--            <el-dropdown-menu class="user-name-wrapper" slot="dropdown">-->
        <!--                <el-dropdown-item>挂号订单</el-dropdown-item>-->
        <!--                <el-dropdown-item>就诊人管理</el-dropdown-item>-->
        <!--                <el-dropdown-item divided>退出登录</el-dropdown-item>-->
        <!--            </el-dropdown-menu>-->
        <!--        </el-dropdown>-->
        <span class="v-link clickable" @click="dialogUserFormVisible = true"
          >登录/注册</span
        >
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      hosname: "",
    };
  },
  created() {},

  methods: {
    // // 在输入框输入，会弹出相关信息
    querySearchAsync(queryString, cb) {
      this.searchObj = {};
      if (queryString == "") return;
      hospitalApi.getByHosname(queryString).then((response) => {
        for (let i = 0, len = response.data.length; i <len; i++) {
          response.data[i].value = response.data[i].hosname;
        }
        cb(response.data);
      });
    },
     // // 点击下拉框的一个内容，执行搜索
    handleSelect(item) {
      window.location.href = '/hosp/' + item.hoscode;
    }
  }
};
</script>
