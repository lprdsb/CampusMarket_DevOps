<template>
    <div class="register-containel">
        <div class="register-panel">
            <!-- 左侧欢迎词 -->
            <div class="register-left">
                <h2>欢迎加入我们！</h2>
                <p>在这里，您可以发现、发布、交流各种校园闲置好物</p>
            </div>

            <!-- 右侧注册表单 -->
            <div class="register-right">
                <h1>注册新身份</h1>
                <div class="input-group">
                    <input v-model="act" placeholder="输入账号（邮箱/用户名）" />
                    <input v-model="name" placeholder="输入昵称" />
                    <input v-model="pwd" type="password" placeholder="输入密码" />
                    <input v-model="pwdConfirm" type="password" placeholder="确认密码" />
                </div>
                <div class="btn-container">
                    <span class="register-btn" @click="registerFunc">立即注册</span>
                </div>
                <div class="tip">
                    <p>已有账户？<span class="no-act" @click="toDoLogin">返回登录</span></p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
const DELAY_TIME = 1300;
import request from "@/utils/request.js";
import md5 from 'js-md5';
import Logo from '@/components/Logo.vue';
export default {
    name: "Register",
    components: { Logo },
    data() {
        return {
            act: '', // 账号
            pwd: '', // 密码
            pwdConfirm: '', // 确认密码
            name: '' // 用户名
        }
    },
    methods: {
        // 返回登录页面
        toDoLogin() {
            this.$router.push('/login');
        },
        async registerFunc() {
            if (!this.act || !this.pwd || !this.pwdConfirm || !this.name) {
                this.$swal.fire({
                    title: '填写校验',
                    text: '账号或密码或用户名不能为空',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            if (this.pwd !== this.pwdConfirm) {
                this.$swal.fire({
                    title: '填写校验',
                    text: '前后密码输入不一致',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            const hashedPwd = md5(md5(this.pwd));
            const paramDTO = { userAccount: this.act, userPwd: hashedPwd, userName: this.name };
            try {
                const { data } = await request.post(`user/register`, paramDTO);
                if (data.code !== 200) {
                    this.$swal.fire({
                        title: '注册失败',
                        text: data.msg,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: DELAY_TIME,
                    });
                    return;
                }
                // 使用Swal通知注册成功，延迟后跳转
                this.$swal.fire({
                    title: '注册成功',
                    text: '即将返回登录页...',
                    icon: 'success',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                // 根据角色延迟跳转
                setTimeout(() => {
                    this.$router.push('/login');
                }, DELAY_TIME);
            } catch (error) {
                console.error('注册请求错误:', error);
            }
        }
    }
};
</script>

<style lang="scss" scoped>
* {
    user-select: none;
    box-sizing: border-box;
}

.register-containel {
    width: 100vw;
    height: 100vh;
    background: linear-gradient(to right, #d8ecff, #f0f8ff);
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px;
}

.register-panel {
    display: flex;
    width: 860px;
    height: 520px;
    background: #ffffff;
    box-shadow: 0 12px 28px rgba(64, 158, 255, 0.2);
    border-radius: 20px;
    overflow: hidden;
}

.register-left {
    flex: 1;
    background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
    color: white;
    padding: 40px 30px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    h2 {
        font-size: 30px;
        margin-bottom: 20px;
        font-weight: 700;
    }

    p {
        font-size: 16px;
        opacity: 0.9;
        line-height: 1.6;
    }
}

.register-right {
    flex: 1;
    padding: 40px 30px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .btn-container {
        display: flex;
        justify-content: center;
    }

    h1 {
        font-size: 26px;
        color: #409EFF;
        margin-bottom: 30px;
        text-align: center;
    }

    .input-group {
        display: flex;
        flex-direction: column;
        gap: 16px;
        margin-bottom: 20px;

        input {
            padding: 14px 18px;
            font-size: 16px;
            border-radius: 8px;
            border: 1px solid #d0e2ff;
            background-color: #f6faff;
            transition: all 0.3s;

            &:focus {
                border-color: #409eff;
                outline: none;
                box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
            }
        }
    }

    .register-btn {
        background: linear-gradient(to right, #409eff, #66b1ff);
        border: none;
        color: white;
        font-weight: 600;
        font-size: 16px;
        border-radius: 8px;
        padding: 14px;
        cursor: pointer;
        transition: all 0.3s ease;
        // align-content: center;

        &:hover {
            opacity: 0.9;
            transform: translateY(-1px);
        }
    }

    .tip {
        margin-top: 20px;
        text-align: center;
        font-size: 14px;
        color: #666;

        .no-act {
            color: #409eff;
            cursor: pointer;
            font-weight: 600;

            &:hover {
                text-decoration: underline;
            }
        }
    }
}
</style>