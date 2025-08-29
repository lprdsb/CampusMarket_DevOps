module.exports = {
    lintOnSave: false,
    assetsDir: 'static',
    parallel: false,
    publicPath: './',
    devServer: {
        disableHostCheck: true,
        host: "localhost",
        port: 31452,
        https: false,
        proxy: {
            // '/interaction-api': {
            //     target: 'http://localhost:31451',
            //     changeOrigin: true, // 如果目标服务器是一个 HTTPS 服务器，则需要设置为 true
            //     pathRewrite: { '^/interaction-api': '' }, // 将 '/api' 前缀重写为空字符串，以便后端服务接收到正确的路径
            // },
            // '/product-api': {
            //     target: 'http://localhost:31450',
            //     changeOrigin: true, // 如果目标服务器是一个 HTTPS 服务器，则需要设置为 true
            //     pathRewrite: { '^/product-api': '' }, // 将 '/api' 前缀重写为空字符串，以便后端服务接收到正确的路径
            // },
            '/user-api': {
                target: 'http://localhost:31451',
                changeOrigin: true, // 如果目标服务器是一个 HTTPS 服务器，则需要设置为 true
                pathRewrite: { '^/user-api': '' }, // 将 '/api' 前缀重写为空字符串，以便后端服务接收到正确的路径
            },
        },

        overlay: {
            warning: false,
            errors: false
        },
    },
}