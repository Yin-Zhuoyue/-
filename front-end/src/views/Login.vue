<template>
    <div id="app" class="">
        <div class="loginStyle">
            <el-form label-position="right">
                <h1 align="center" style="font-size: 40px">取数任务自动化系统</h1>
                <h3 align="center" style="font-size: 30px">用户登录</h3>
                <el-form-item>
                    <el-input type="text" v-model="loginForm.user_name" placeholder="请输入用户名"
                              style="font-size: 20px"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input type="password" v-model="loginForm.user_password" placeholder="请输入密码"
                              style="font-size: 20px"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" style="font-size: 20px" @click="login()">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data(){
            return {
                loginForm: {}
            }
        },
        methods: {
            login() {
                this.$axios.post("/login", this.loginForm).then((res) => {
                    if (res.data.code === 200) {
                        window.sessionStorage.setItem('token',res.data.data);
                        window.sessionStorage.setItem('user_name',this.loginForm.user_name);
                        this.$router.push({path:"/task"})
                    } else {
                        this.$message.error(res.data.msg);
                    }
                })
            }
        }

    }
</script>

<style>
    html, body {
        height: 100%;
    }

    body {
        margin: 0;
    }

    #app {
        height: 100%;
        margin: 0;
        padding: 0;
        text-align: center;
    }

    .loginStyle {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        background-image: url("../assets/OIP-C.jpg");
        background-size: 100% 100%;
        background-repeat: no-repeat;
    }

</style>