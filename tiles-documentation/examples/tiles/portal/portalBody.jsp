<%@ taglib uri="/WEB-INF/tiles.tld" prefix="tiles" %>

<tiles:insert page="/layout/columnsLayout.jsp" flush="true">
  <tiles:put name="numCols" value="2" />
  <tiles:putList name="list0" >
    <tiles:add value="/tutorial/portal/login.jsp" />
    <tiles:add value="/tutorial/portal/messages.jsp" />
    <tiles:add value="/tutorial/portal/newsFeed.jsp" />
    <tiles:add value="/tutorial/portal/advert2.jsp" />
  </tiles:putList>
  <tiles:putList name="list1" >
    <tiles:add value="/tutorial/portal/advert3.jsp" />
    <tiles:add value="/tutorial/portal/stocks.jsp" />
    <tiles:add value="/tutorial/portal/whatsNew.jsp" />
    <tiles:add value="/tutorial/portal/personalLinks.jsp" />
    <tiles:add value="/tutorial/portal/search.jsp" />
  </tiles:putList>
</tiles:insert>