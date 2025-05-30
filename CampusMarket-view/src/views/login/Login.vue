<template>
    <div class="login-container">
      <div class="login-panel">
        <div class="image-box">
          <img src="/bag.png" class="login-image" />
        </div>
        <div class="right-login">
          <h2 class="title">校园跳蚤交易平台</h2>
          <input v-model="act" class="input-field" placeholder="输入账号" />
          <input v-model="pwd" class="input-field" type="password" placeholder="输入密码" />
          <button class="login-btn" @click="login">立即登录</button>
          <p class="tip">没有账号？<span class="no-act" @click="toDoRegister">点此注册</span></p>
        </div>
      </div>
    </div>
  </template>

<script>
const DELAY_TIME = 1300;
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";
import md5 from 'js-md5';
import Logo from '@/components/Logo.vue';
export default {
    name: "Login",
    components: { Logo },
    data() {
        return {
            act: '',
            pwd: '',
            colorLogo: 'rgb(38,38,38)',
        }
    },
    methods: {
        // 跳转注册页面
        toDoRegister() {
            this.$router.push('/register');
        },
        async login() {
            if (!this.act || !this.pwd) {
                this.$swal.fire({
                    title: '填写校验',
                    text: '账号或密码不能为空',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            const hashedPwd = md5(md5(this.pwd));
            const paramDTO = { userAccount: this.act, userPwd: hashedPwd };
            try {
                const { data } = await request.post(`user/login`, paramDTO);
                if (data.code !== 200) {
                    this.$swal.fire({
                        title: '登录失败',
                        text: data.msg,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: DELAY_TIME,
                    });
                    return;
                }
                setToken(data.data.token);
                // 根据角色延迟跳转
                setTimeout(() => {
                    const { role } = data.data;
                    this.navigateToRole(role);
                }, DELAY_TIME);
            } catch (error) {
                console.error('登录请求错误:', error);
                this.$message.error('登录请求出错，请重试！');
            }
        },
        navigateToRole(role) {
            switch (role) {
                case 1:
                    this.$router.push('/admin');
                    break;
                case 2:
                    console.log("用户角色：", role);
                    this.$router.push('/user');
                    break;
                default:
                    console.warn('未知的角色类型:', role);
                    break;
            }
        },
    }
};
</script>

<style scoped lang="scss">
* {
  user-select: none;
}

.login-container {
  width: 100%;
  min-height: 100vh;
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.login-panel {
  display: flex;
  padding: 20px;
  border-radius: 10px;
  background: #66ccff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.image-box {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  width: 300px;
}

.login-image {
  width: 300px;
  max-width: 100%;
}

.right-login {
  width: 280px;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: bold;
}

.input-field {
  width: 100%;
  height: 48px;
  margin-top: 12px;
  padding: 0 15px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background-color: #f8f8f8;
}

.input-field:focus {
  outline: none;
  background-color: #f0f0f0;
  border-color: #1ebe5e;
}

.login-btn {
  margin-top: 20px;
  width: 100%;
  height: 43px;
  background-color: #1ebe5e;
  color: #fff;
  font-size: 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #19b457;
}

.tip {
  margin-top: 16px;
  font-size: 14px;
  color: #647897;
  text-align: center;

  .no-act {
    color: #3e77c2;
    margin-left: 5px;
    cursor: pointer;
  }

  .no-act:hover {
    text-decoration: underline;
  }
}
</style>
