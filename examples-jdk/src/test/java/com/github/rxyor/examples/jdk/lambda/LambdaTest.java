package com.github.rxyor.examples.jdk.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSON;

import com.github.rxyor.examples.jdk.lambda.LambdaTest.Product.Sku;
import com.github.rxyor.examples.jdk.lambda.LambdaTest.Product.Sku.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LambdaTest {
    static List<Product> productList = new ArrayList<>();

    @BeforeEach
    public void init() {
        for (int i = 0; i < 4; i++) {
            Product product = new Product();
            product.setNo("productNo" + i);
            product.setName("productName" + i);
            product.setSkuList(new ArrayList<Sku>());
            productList.add(product);
            for (int j = 0; j < 4; j++) {
                Sku sku = new Sku();
                sku.setSkuId("skuId" + j);
                if (j % 3 != 0) {
                    sku.setPropertyList(new ArrayList<Property>());
                    for (int k = 0; k < 4; k++) {
                        Property property = new Property();
                        property.setName("propertyName" + k);
                        property.setValue("propertyValue" + k);
                        sku.getPropertyList().add(property);
                    }
                }
                product.getSkuList().add(sku);
            }
        }
    }

    @Test
    public void testLambdaFlatMap() {
        List<Property> propertyList = productList.stream()
            .flatMap(product -> product.getSkuList().stream())
            .flatMap(sku -> sku.getPropertyList().stream())
            .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(propertyList));
    }

    @Test
    public void testLambdaFlatMap2() {
        List<Property> propertyList = productList.stream()
            .flatMap(product -> product.getSkuList() != null ? product.getSkuList().stream() : Stream.empty())
            .flatMap(sku -> sku.getPropertyList() != null ? sku.getPropertyList().stream() : Stream.empty())
            .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(propertyList));
    }

    public static class Product {
        private String no;
        private String name;
        private List<Sku> skuList;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Sku> getSkuList() {
            return skuList;
        }

        public void setSkuList(List<Sku> skuList) {
            this.skuList = skuList;
        }

        public static class Sku {
            private String skuId;
            private List<Property> propertyList;

            public String getSkuId() {
                return skuId;
            }

            public void setSkuId(String skuId) {
                this.skuId = skuId;
            }

            public List<Property> getPropertyList() {
                return propertyList;
            }

            public void setPropertyList(
                List<Property> propertyList) {
                this.propertyList = propertyList;
            }

            public static class Property {
                private String name;
                private String value;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
