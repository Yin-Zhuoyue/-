module.exports = {
    // publicPath: './',
    devServer: {
        open: true,
        host: 'localhost',
        port: 8087,
        https: false,
        proxy: { //配置跨域
            '/api': {
                target: 'http://localhost:8086',
                ws: true,
                changOrigin: true, //允许跨域
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }
};
