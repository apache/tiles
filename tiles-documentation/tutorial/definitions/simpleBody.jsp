<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/layout/columnsLayout.jsp" flush="true">
  <tiles:put name="numCols" value="2" />
  <tiles:putList name="list0" >
    <tiles:add value="/portal/login.jsp" />
    <tiles:add value="/portal/messages.jsp" />
    <tiles:add value="/portal/newsFeed.jsp" />
    <tiles:add value="/portal/advert2.jsp" />
  </tiles:putList>
  <tiles:putList name="list1" >
    <tiles:add value="/portal/advert3.jsp" />
    <tiles:add value="/portal/stocks.jsp" />
    <tiles:add value="/portal/whatsNew.jsp" />
    <tiles:add value="/portal/personalLinks.jsp" />
    <tiles:add value="/portal/search.jsp" />
  </tiles:putList>
</tiles:insert>
