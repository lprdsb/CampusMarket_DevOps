<template>
    <div class="product-list">
      <div class="clear-message">
        <el-tooltip content="清除浏览记录" placement="top">
          <span @click="clearView">
            <i class="el-icon-delete"></i>
          </span>
        </el-tooltip>
      </div>
  
      <el-row v-if="productList.length === 0" justify="center">
        <el-empty description="浏览记录为空"></el-empty>
      </el-row>
  
      <el-row v-else :gutter="20" justify="center">
        <el-col :span="6" v-for="(product, index) in productList" :key="index">
          <div class="item-product" @click="route(product)">
            <div class="cover">
              <img :src="coverListParse(product)" alt="商品图片" />
              <span class="bargain">{{ product.isBargain ? '支持砍价' : '不支持砍价' }}</span>
            </div>
            <div class="info">
              <div class="title">{{ product.name }}</div>
              <div class="price-row">
                <span class="decimel-symbol">¥</span>
                <span class="price">{{ product.price }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
</template>
<script>
export default {
    name: 'MyProduct',
    data() {
        return {
            productList: []
        };
    },
    created() {
        this.fetchProduct();
    },
    methods: {
        clearView() {
            this.$axios.post(`/interaction/batchDeleteView`).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '足迹清除成功',
                        message: data.msg,
                        type: 'success'
                    });
                    this.fetchProduct();
                }
            }).catch(error => {
                console.log("商品足迹清除异常：", error);
            })
        },
        route(product) {
            // 跳转商品详情
            this.$router.push('/product-detail?productId=' + product.id);
        },
        /**
         * 商品封面图处理
         * 从字符串转成可用数组
         * @param {*} product 待处理商品信息
         */
        coverListParse(product) {
            if (product.coverList === null) {
                return;
            }
            const newCoverList = product.coverList.split(',');
            return newCoverList[0];
        },
        /**
         * 查询用户自己发布的商品信息
         */
        fetchProduct() {
            this.$axios.post('/interaction/myView').then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.productList = data.data;
                }
            }).catch(error => {
                console.log("商品查询异常：", error);
            })
        },
    }
};
</script>
<style scoped lang="scss">
.product-list {
  padding: 30px;

  .clear-message {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;

    span {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 36px;
      height: 36px;
      border-radius: 50%;
      background-color: #e3f2fd;
      color: #007bff;
      cursor: pointer;
      transition: background-color 0.3s ease;

      &:hover {
        background-color: #cce4fb;
      }

      i {
        font-size: 18px;
      }
    }
  }

  .item-product {
    background: #ffffff;
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.3s ease;
    box-shadow: 0 2px 6px rgba(0, 123, 255, 0.1);
    cursor: pointer;

    &:hover {
      box-shadow: 0 6px 20px rgba(0, 123, 255, 0.2);
      transform: translateY(-4px);
    }

    .cover {
      position: relative;

      img {
        width: 100%;
        height: 240px;
        object-fit: cover;
        border-radius: 12px 12px 0 0;
      }

      .bargain {
        position: absolute;
        top: 10px;
        left: 10px;
        background-color: #d0eaff;
        color: #007bff;
        font-size: 12px;
        font-weight: bold;
        padding: 4px 8px;
        border-radius: 6px;
      }
    }

    .info {
      padding: 14px;

      .title {
        font-size: 18px;
        font-weight: 600;
        color: #333;
        margin-bottom: 8px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .price-row {
        display: flex;
        align-items: baseline;
        gap: 4px;

        .decimel-symbol {
          font-size: 14px;
          color: #007bff;
          font-weight: bold;
        }

        .price {
          font-size: 22px;
          color: #007bff;
          font-weight: bold;
        }
      }
    }
  }
}
</style>