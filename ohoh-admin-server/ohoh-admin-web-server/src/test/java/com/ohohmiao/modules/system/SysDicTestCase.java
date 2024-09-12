package com.ohohmiao.modules.system;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.ohohmiao.BaseTestCase;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.modules.system.model.entity.SysDic;
import com.ohohmiao.modules.system.service.SysDicService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 系统字典相关测试用例
 *
 * @author ohohmiao
 * @date 2023-05-26 14:33
 */
@Slf4j
public class SysDicTestCase extends BaseTestCase {

    @Resource
    private SysDicService sysDicService;

    @Test
    public void impNationalDistrict() {
        String qgqhUrlPrefix = "http://www.stats.gov.cn/sj/tjbz/tjyqhdmhcxhfdm/2022/";

        //省份
        String proviceHtml = HttpUtil.get(qgqhUrlPrefix + "index.html");
        Document proviceDocument = Jsoup.parse(proviceHtml);
        Elements provinceTrs = proviceDocument.select(".provincetr");
        int provinceCount = 0;
        for(Element provinceTrElement: provinceTrs) {

            List<SysDic> dicList = CollectionUtil.newArrayList();

            Elements provinceATags = provinceTrElement.select("a");
            for(Element provinceATag: provinceATags) {
                String provinceName = provinceATag.html().replace("<br>", "");
                String provinceUrl = provinceATag.attr("href");
                String provinceCode = provinceUrl.replace(".html", "") + "0000000000";
                log.info(provinceName+"#"+provinceCode+"#"+provinceUrl);
                SysDic provinceDic = new SysDic();
                provinceDic.setDictypeCode("Province");
                provinceDic.setDicName(provinceName);
                provinceDic.setDicCode(provinceCode);
                provinceDic.setDicSort(++provinceCount);
                provinceDic.setDeleteFlag(CommonWhetherEnum.NO.getCode());
                provinceDic.setCreateUserid("1");
                provinceDic.setCreateTime(new Date());
                dicList.add(provinceDic);
                //城市
                String cityHtml = HttpUtil.get(qgqhUrlPrefix + provinceUrl);
                Document cityDocument = Jsoup.parse(cityHtml);
                Elements cityTrs = cityDocument.select(".citytr");
                for(int cityIndex = 0; cityIndex < cityTrs.size(); cityIndex++){
                    Element cityTrElement = cityTrs.get(cityIndex);
                    Elements cityATags = cityTrElement.select("a");
                    Element cityCodeATag = cityATags.get(0);
                    Element cityNameATag = cityATags.get(1);
                    String cityName = cityNameATag.html().replace("<br>", "");
                    String cityCode = cityCodeATag.html().replace("<br>", "");
                    String cityUrl = cityNameATag.attr("href");
                    log.info(cityName+"#"+cityCode+"#"+cityUrl);
                    SysDic cityDic = new SysDic();
                    cityDic.setDictypeCode("City");
                    cityDic.setDicName(cityName);
                    cityDic.setDicCode(cityCode);
                    cityDic.setDicSort(cityIndex + 1);
                    cityDic.setDeleteFlag(CommonWhetherEnum.NO.getCode());
                    cityDic.setCreateUserid("1");
                    cityDic.setCreateTime(new Date());
                    dicList.add(cityDic);
                    //区县
                    String countyHtml = HttpUtil.get(qgqhUrlPrefix + cityUrl);
                    Document countyDocument = Jsoup.parse(countyHtml);
                    Elements countyTrs = countyDocument.select(".countytr");
                    for(int countyIndex = 0; countyIndex < countyTrs.size(); countyIndex++){

                        Element countyTrElement = countyTrs.get(countyIndex);
                        Elements countyATags = countyTrElement.select("a");
                        if(countyATags.size() == 0){
                            Elements countyTds = countyTrElement.select("td");
                            Element countyNameTd = countyTds.get(1);
                            Element countyCodeTd = countyTds.get(0);
                            String countyName = countyNameTd.html().replace("<br>", "");
                            String countyCode = countyCodeTd.html().replace("<br>", "");
                            log.info(countyName+"#"+countyCode);
                            SysDic countyDic = new SysDic();
                            countyDic.setDictypeCode("County");
                            countyDic.setDicName(countyName);
                            countyDic.setDicCode(countyCode);
                            countyDic.setDicSort(countyIndex + 1);
                            countyDic.setDeleteFlag(CommonWhetherEnum.NO.getCode());
                            countyDic.setCreateUserid("1");
                            countyDic.setCreateTime(new Date());
                            dicList.add(countyDic);
                        }else{
                            Element countyCodeATag = countyATags.get(0);
                            Element countyNameATag = countyATags.get(1);
                            String countyName = countyNameATag.html().replace("<br>", "");
                            String countyCode = countyCodeATag.html().replace("<br>", "");
                            String countyUrl = countyNameATag.attr("href");
                            log.info(countyName+"#"+countyCode+"#"+countyUrl);
                            SysDic countyDic = new SysDic();
                            countyDic.setDictypeCode("County");
                            countyDic.setDicName(countyName);
                            countyDic.setDicCode(countyCode);
                            countyDic.setDicSort(countyIndex + 1);
                            countyDic.setDeleteFlag(CommonWhetherEnum.NO.getCode());
                            countyDic.setCreateUserid("1");
                            countyDic.setCreateTime(new Date());
                            dicList.add(countyDic);

                            String proviceTwoBitCode = countyCode.substring(0, 2);

                            //乡镇、街道
                            String townHtml = HttpUtil.get(qgqhUrlPrefix + proviceTwoBitCode + "/" + countyUrl);
                            Document townDocument = Jsoup.parse(townHtml);
                            Elements townTrs = townDocument.select(".towntr");
                            for(int townIndex = 0; townIndex < townTrs.size(); townIndex++){
                                Element townTrElement = townTrs.get(townIndex);
                                Elements townATags = townTrElement.select("a");
                                if(townATags.size() == 0){
                                    Elements townTds = townTrElement.select("td");
                                    Element townNameTd = townTds.get(1);
                                    Element townCodeTd = townTds.get(0);
                                    String townName = townNameTd.html().replace("<br>", "");
                                    String townCode = townCodeTd.html().replace("<br>", "");
                                    log.info(townName+"#"+townCode);
                                    SysDic townDic = new SysDic();
                                    townDic.setDictypeCode("Town");
                                    townDic.setDicName(townName);
                                    townDic.setDicCode(townCode);
                                    townDic.setDicSort(townIndex + 1);
                                    townDic.setDeleteFlag(CommonWhetherEnum.NO.getCode());
                                    townDic.setCreateUserid("1");
                                    townDic.setCreateTime(new Date());
                                    dicList.add(townDic);
                                }else{
                                    Element townCodeATag = townATags.get(0);
                                    Element townNameATag = townATags.get(1);
                                    String townName = townNameATag.html().replace("<br>", "");
                                    String townCode = townCodeATag.html().replace("<br>", "");
                                    String townUrl = townNameATag.attr("href");
                                    log.info(townName+"#"+townCode+"#"+townUrl);
                                    SysDic townDic = new SysDic();
                                    townDic.setDictypeCode("Town");
                                    townDic.setDicName(townName);
                                    townDic.setDicCode(townCode);
                                    townDic.setDicSort(townIndex + 1);
                                    townDic.setDeleteFlag(CommonWhetherEnum.NO.getCode());
                                    townDic.setCreateUserid("1");
                                    townDic.setCreateTime(new Date());
                                    dicList.add(townDic);

                                    String cityTwoBitCode = townCode.substring(2, 4);

                                    //社区、居委会、村委会
                                    /*String villageHtml = HttpUtil.get(qgqhUrlPrefix + proviceTwoBitCode + "/" + cityTwoBitCode + "/" + townUrl);
                                    Document villageDocument = Jsoup.parse(villageHtml);
                                    Elements villageTrs = villageDocument.select(".villagetr");
                                    for(int villageIndex = 0; villageIndex < villageTrs.size(); villageIndex++){
                                        Element villageTrElement = villageTrs.get(villageIndex);
                                        Elements villageTds = villageTrElement.select("td");
                                        Element villageNameTd = villageTds.get(2);
                                        Element villageCodeTd = villageTds.get(0);
                                        String villageName = villageNameTd.html().replace("<br>", "");
                                        String villageCode = villageCodeTd.html().replace("<br>", "");
                                        log.info(villageName+"#"+villageCode);
                                        SysDic villageDic = new SysDic();
                                        villageDic.setDictypeCode("Village");
                                        villageDic.setDicName(villageName);
                                        villageDic.setDicCode(villageCode);
                                        villageDic.setDicSort(villageIndex + 1);
                                        villageDic.setDeleteFlag(WhetherEnum.NO.getCode());
                                        villageDic.setCreateUserid("1");
                                        villageDic.setCreateTime(new Date());
                                        dicList.add(villageDic);
                                    }*/
                                }
                            }

                        }
                    }
                }
            }

            sysDicService.saveBatch(dicList, 1000);

        }

    }

}
