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
     <!-- 登录弹出层 -->
    <el-dialog :visible.sync="dialogUserFormVisible" style="text-align: left;" top="50px" :append-to-body="true"  width="960px" @close="closeDialog()">
      <div class="container">

        <!-- 手机登录 #start -->
        <div class="operate-view" v-if="dialogAtrr.showLoginType === 'phone'">
          <div class="wrapper" style="width: 100%">
            <div class="mobile-wrapper" style="position: static;width: 70%">
              <span class="title">{{ dialogAtrr.labelTips }}</span>
              <el-form>
                <el-form-item>
                  <el-input v-model="dialogAtrr.inputValue" :placeholder="dialogAtrr.placeholder" :maxlength="dialogAtrr.maxlength" class="input v-input">
                    <span slot="suffix" class="sendText v-link" v-if="dialogAtrr.second > 0">{{ dialogAtrr.second }}s </span>
                    <span slot="suffix" class="sendText v-link highlight clickable selected" v-if="dialogAtrr.second == 0" @click="getCodeFun()">重新发送 </span>
                  </el-input>
                </el-form-item>
              </el-form>
              <div class="send-button v-button" @click="btnClick()"> {{ dialogAtrr.loginBtn }}</div>
            </div>
            <div class="bottom">
              <div class="wechat-wrapper" @click="weixinLogin()"><span
                class="iconfont icon"></span></div>
              <span class="third-text"> 第三方账号登录 </span></div>
          </div>
        </div>
        <!-- 手机登录 #end -->

        <!-- 微信登录 #start -->
        <div class="operate-view"  v-if="dialogAtrr.showLoginType === 'weixin'" >
          <div class="wrapper wechat" style="height: 400px">
            <div>
              <div id="weixinLogin"></div>
            </div>
            <div class="bottom wechat" style="margin-top: -80px;">
              <div class="phone-container">
                <div class="phone-wrapper"  @click="phoneLogin()"><span
                  class="iconfont icon"></span></div>
                <span class="third-text"> 手机短信验证码登录 </span></div>
            </div>
          </div>
        </div>
        <!-- 微信登录 #end -->

        <div class="info-wrapper">
          <div class="code-wrapper">
            <div><img src="//img.114yygh.com/static/web/code_login_wechat.png" class="code-img">
              <div class="code-text"><span class="iconfont icon"></span>微信扫一扫关注
              </div>
              <div class="code-text"> “快速预约挂号”</div>
            </div>
            <div class="wechat-code-wrapper"><img
              src="//img.114yygh.com/static/web/code_app.png"
              class="code-img">
              <div class="code-text"> 扫一扫下载</div>
              <div class="code-text"> “预约挂号”APP</div>
            </div>
          </div>
          <div class="slogan">
            <div>xxxxxx官方指定平台</div>
            <div>快速挂号 安全放心</div>
          </div>
        </div>
      </div>
    </el-dialog>

  </div>
</template>
<script>
export default {
  data() {
    return {
      hosname: '',
      searchObj:{}
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
