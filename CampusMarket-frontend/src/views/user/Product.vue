<template>
    <div>
        <!-- <div class="nav-category">
            <div class="left">
                <span :style="{
                    color: categorySelectedItem.name === isUseCategory.name ? '#fff' : '',
                    backgroundColor: categorySelectedItem.name === isUseCategory.name ? '#409EFF' : ''
                }" @click="categorySelected(isUseCategory)" :key="index"
                    v-for="(isUseCategory, index) in isUseCategoryList">
                    {{ isUseCategory.name }}
                </span>
            </div>
        </div> -->

        <!-- <div class="search-bar">
            <div class="word-search">
                <div class="item">
                    <input type="text" placeholder="搜索商品" v-model="key">
                    <i class="el-icon-search" @click="fetch"></i>
                </div>
            </div>
        </div> -->
        <div class="nav-category">
            <div class="right">
                <!-- <span class="bargain">
                    <span :style="{
                        color: bargainSelectedItem.name === bargain.name ? '#fff' : '',
                        backgroundColor: bargainSelectedItem.name === bargain.name ? '#409EFF' : ''
                    }" @click="bargainSelected(bargain)" v-for="(bargain, index) in bargainStatus" :key="index">{{
                        bargain.name }}</span>
                </span> -->
                <el-date-picker style="width: 216px;margin-right: 5px;" @change="fetchFreshData" size="small"
                    v-model="searchTime" type="daterange" range-separator="至" start-placeholder="发布开始"
                    end-placeholder="发布结束">
                </el-date-picker>
                <el-input style="width: 100px;margin-right: 5px;" size="small" v-model="productQueryDto.priceMin"
                    placeholder="最低价" @change="val => handlePriceChange('priceMin', val)">
                </el-input>
                <span style="margin-right: 5px;">-</span>
                <el-input style="width: 100px;margin-right: 5px;" size="small" v-model="productQueryDto.priceMax"
                    placeholder="最高价" @change="val => handlePriceChange('priceMax', val)">
                </el-input>
                <el-select style="width: 100px;margin-right: 5px;" @change="fetchFreshData" size="small"
                    v-model="productQueryDto.categoryId" placeholder="商品类别">
                    <el-option v-for="item in isUseCategoryList" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </div>
        </div>
        <div class="product-list">
            <el-row v-if="productList.length === 0">
                <el-empty description="暂无商品信息"></el-empty>
            </el-row>
            <el-row v-else>
                <el-col @click.native="route(product)" :span="6" v-for="(product, index) in productList" :key="index">
                    <div class="item-product" style="position: relative;">

                        <div class="cover">
                            <img :src="coverListParse(product)" alt="" srcset="">
                            <div v-if="product.isRecommended" class="recommend-tag"
                                style="position: absolute; right: 10px; top: 10px;">
                                猜你喜欢
                            </div>
                            <div class="price-box">
                                <!-- <div style="padding-block: 15px;position: absolute; left: 20px; top: 10px;"> -->
                                <span class="decimel-symbol">¥</span>
                                <span class="price">{{ product.price }}</span>
                                <!-- </div> -->
                            </div>
                        </div>

                        <div style="display: flex;justify-content: left;gap: 4px;align-items: center;">
                            <span class="title">
                                {{ product.name }}
                            </span>
                            <!-- <span class="bargain-hover">{{ product.isBargain ? '支持砍价' : '不支持砍价' }}</span> -->
                        </div>
                        <div class="info">
                            <img :src="product.userAvatar" alt="" srcset="">
                            <span>{{ product.userName }}</span>
                            <span class="love">{{ product.likeNumber }}人想要</span>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>
<script>
import { getToken, setUserInfo, setSearchKey } from "@/utils/storage";
export default {
    name: 'Product',
    data() {
        return {
            categoryList: [], // 存储的商品类别数组
            isUseCategoryList: [], // 存储的启用的类别数组
            categorySelectedItem: {},
            productQueryDto: {
                priceMin: null,
                priceMax: null
            }, // 商品查询条件类
            productList: [],// 存储后端返回的商品数据列表
            recommendedProducts: [], // 新增推荐列表
            bargainSelectedItem: {},
            searchTime: [],
            searchPath: '/search',
            bargainStatus: [{ isBargain: null, name: '全部' }, { isBargain: true, name: '支持砍价' }, { isBargain: false, name: '不支持砍价' }]

        };
    },
    created() {
        this.fetchFreshData();
        this.fetchCategoryList();
        // 页面加载时，默认不启用砍价查询条件
        this.bargainSelected(this.bargainStatus[0]);
    },
    methods: {
        route(product) {
            // 跳转商品详情
            this.$router.push('/product-detail?productId=' + product.id);
        },
        // 搜索
        fetch() {
            setSearchKey(this.key);
            this.handleRouteSelect(this.searchPath);
        },

        handleRouteSelect(path) {
            // console.log(path);
            if (this.$router.currentRoute.fullPath !== path) {
                this.$router.push(path);
            }
        },
        coverListParse(product) {
            if (product.coverList === null) {
                return;
            }
            const newCoverList = product.coverList.split(',');
            return newCoverList[0];
        },
        /**
         * 商品砍价选中事件
         * @param {*} bargain 
         */
        bargainSelected(bargain) {
            this.bargainSelectedItem = bargain;
            this.productQueryDto.isBargain = bargain.isBargain;
            this.fetchFreshData();
        },
        /**
         * 查询商品数据
         */
        async fetchFreshData() {
            let startTime = null;
            let endTime = null;
            if (this.searchTime != null && this.searchTime.length === 2) {
                const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                startTime = `${startDate.split('T')[0]}T00:00:00`;
                endTime = `${endDate.split('T')[0]}T23:59:59`;
            }
            this.productQueryDto.startTime = startTime;
            this.productQueryDto.endTime = endTime;
            this.$axios.post('/product-api/product/query', this.productQueryDto).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.productList = data.data;
                }
                this.$axios.get('/product-api/product/recommend').then(recommendRes => {
                    const { data } = recommendRes;
                    if (data.code === 200) {
                        this.recommendedProducts = data.data;
                    }
                    this.mergeProducts();
                }).catch(error => {
                    console.log("推荐商品查询异常：", error);
                });
            }).catch(error => {
                console.log("商品查询异常：", error);
            });
        },
        mergeProducts() {
            const recommendedIds = new Set(
                this.recommendedProducts.map(p => p.id)
            );

            const mergedList = this.productList.map(product => {
                return {
                    ...product,
                    isRecommended: recommendedIds.has(product.id)
                }
            });
            // 按推荐状态分组并优先排列推荐商品
            const recommended = mergedList.filter(p => p.isRecommended);
            const nonRecommended = mergedList.filter(p => !p.isRecommended);
            this.productList = [...recommended, ...nonRecommended];
        },
        /**
         * 商品类别选中事件
         * @param {*} category 
         */
        categorySelected(category) {
            this.categorySelectedItem = category;
            this.productQueryDto.categoryId = category.id;
            // 查询对应的商品分类下面的商品数据
            this.fetchFreshData();
        },
        /**
         * 加载商品类别数据
         */
        fetchCategoryList() {
            this.$axios.post('/product-api/category/query', {}).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.categoryList = data.data;
                    this.isUseCategoryList = data.data.filter(category => category.isUse);
                    this.isUseCategoryList.unshift({ id: null, name: '全部' });
                    this.categorySelected(this.isUseCategoryList[0]);
                }
            }).catch(error => {
                console.log("商品类别查询异常：", error);
            })
        },
        handlePriceChange(field, value) {
            if (value === '' || isNaN(value)) {
                this.productQueryDto[field] = null
            } else {
                const numValue = Number(value)
                this.productQueryDto[field] = numValue >= 0 ? numValue : null
            }
            this.fetchFreshData()
        }
    }
};
</script>
<style scoped lang="scss">
.cover {
    img {
        width: 100%;
        height: 350px;
        border-radius: 10px;
        object-fit: cover;
    }

    .price-box {
        position: absolute;
        left: 15px;
        top: 15px;
        background-color: rgba(255, 255, 255, 0.8);
        padding: 6px 12px;
        border-radius: 6px;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        display: flex;
        align-items: baseline;
        backdrop-filter: blur(4px); // 可选：增加磨砂玻璃质感
    }

    .decimel-symbol {
        font-size: 14px;
        color: #007bff;
        margin-right: 2px;
    }

    .price {
        font-size: 18px;
        font-weight: bold;
        color: #007bff;
    }
}

.bargain-hover {
    font-size: 12px;
    font-weight: 600;
    background-color: #e8f3ff;
    color: #409EFF;
    border-radius: 3px;
    padding: 2px 6px;
}

.title {
    font-size: 18px;
    color: #1f1f1f;
    font-weight: 600;
}

.decimel-symbol {
    font-size: 14px;
    color: #409EFF;
    font-weight: 700;
}

.price {
    font-size: 22px;
    color: #409EFF;
    font-weight: 700;
    margin-right: 6px;
}

.love {
    font-size: 13px;
    color: #999;
}

.info {
    display: flex;
    align-items: center;
    gap: 6px;

    img {
        width: 20px;
        height: 20px;
        border-radius: 50%;
    }

    span {
        font-size: 14px;
        color: #666;
    }
}

.bargain {
    display: inline-block;
    font-size: 12px;
    background-color: #f5f7fa;
    line-height: 24px;
    padding: 4px 10px;
    margin-right: 5px;
    border-radius: 6px;
    cursor: pointer;

    span {
        display: inline-block;
        padding: 2px 10px;
        border-radius: 6px;
    }

    span:hover {
        background-color: #ecf5ff;
        color: #409EFF;
    }
}

.product-list {
    padding-block: 20px;

    .item-product {
        padding: 10px;
        border-radius: 12px;
        transition: all 0.3s;
        cursor: pointer;
        background-color: #fff;
        border: 1px solid #f0f0f0;
    }

    .item-product:hover {
        box-shadow: 0 2px 10px rgba(64, 158, 255, 0.1);
        border-color: #d9ecff;
    }
}

.nav-category {
    margin-block: 12px;
    display: flex;
    justify-content: space-between;

    .left {
        display: flex;
        gap: 6px;

        span {
            display: inline-block;
            background-color: #f5f7fa;
            padding: 6px 12px;
            cursor: pointer;
            border-radius: 20px;
            font-size: 13px;
            color: #409EFF;
            transition: background-color 0.3s, color 0.3s;
        }

        span:hover {
            background-color: #ecf5ff;
        }
    }
}

.recommend-tag {
    top: 10px;
    right: 10px;
    background: #ecf5ff;
    color: #409EFF;
    padding: 4px 8px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 600;
    z-index: 1;
}

.search-bar {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
}

.word-search {
    .item {
        background-color: #DBEAFE;
        border-radius: 20px;
        display: flex;
        align-items: center;
        border: 1px solid #93C5FD;

        input {
            border: none;
            background-color: transparent;
            padding: 8px 10px;
            font-size: 14px;
            outline: none;
        }

        i {
            cursor: pointer;
            padding: 6px 10px;
            border-radius: 0 20px 20px 0;
            background-color: #93C5FD;
            color: #fff;
        }

        i:hover {
            background-color: #60A5FA;
        }
    }
}
</style>