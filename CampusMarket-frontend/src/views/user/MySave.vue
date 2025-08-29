<template>
  <div class="product-list">
    <el-row v-if="productList.length === 0" justify="center">
      <el-empty description="暂无商品信息"></el-empty>
    </el-row>
    <el-row v-else :gutter="20" justify="center">
      <el-col :span="6" v-for="(product, index) in productList" :key="index">
        <div class="item-product" @click="route(product)">
          <div class="cover">
            <img :src="coverListParse(product)" alt="商品封面" />
            <!-- <span class="bargain">{{ product.isBargain ? '支持砍价' : '不支持砍价' }}</span> -->
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
import { setProductInfo } from "@/utils/storage"
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
      this.$axios.post('/interaction/queryUser').then(res => {
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

  .item-product {
    background-color: #ffffff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 123, 255, 0.1);
    transition: all 0.3s ease;
    cursor: pointer;

    &:hover {
      box-shadow: 0 6px 18px rgba(0, 123, 255, 0.2);
      transform: translateY(-4px);
    }

    .cover {
      position: relative;

      img {
        width: 100%;
        height: 220px;
        object-fit: cover;
        border-bottom: 1px solid #eee;
        border-radius: 12px 12px 0 0;
      }

      .bargain {
        position: absolute;
        top: 10px;
        left: 10px;
        background-color: #e3f2fd;
        color: #007bff;
        font-size: 12px;
        font-weight: bold;
        padding: 4px 8px;
        border-radius: 6px;
      }
    }

    .info {
      padding: 15px;

      .title {
        font-size: 18px;
        font-weight: 600;
        color: #1f1f1f;
        margin-bottom: 10px;
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