module.exports = {
    lintOnSave: false,
    assetsDir: 'static',
    parallel: false,
    publicPath: './',
    devServer: {
        disableHostCheck: true,
        host: "182.92.140.82",
        port: 11452,
        https: false,
        proxy: {
            '/api': {
                target: 'http://182.92.140.82:11451',
                changeOrigin: true, // 如果目标服务器是一个 HTTPS 服务器，则需要设置为 true
                pathRewrite: { '^/api': '' }, // 将 '/api' 前缀重写为空字符串，以便后端服务接收到正确的路径
            },
        },

        overlay: {
            warning: false,
            errors: false
        },
    },
}