package com.crawl.crawler4j;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	 public static void main(String[] args) throws Exception {
		 HashMap<Integer, String> statCodes = new HashMap<Integer, String>();
		 statCodes.put(200, "OK");
		 statCodes.put(301, "Moved Permanently");
		 statCodes.put(302, "Found");
		 statCodes.put(303, "See Other");
		 
		 statCodes.put(304, "Not Modified");
		 statCodes.put(305, "Use Proxy");
		 statCodes.put(306, "Switch Proxy");
		 statCodes.put(307, "Temporary Redirect");
		 statCodes.put(308, "Permanent Redirect");
		 statCodes.put(400, "Bad request");
		 statCodes.put(401, "Unauthorised");
		 statCodes.put(403, "Forbidden");
		 statCodes.put(404, "Not Found");
		 statCodes.put(405, "Method Not Allowed");
		 statCodes.put(406, "Not Acceptable");
		 statCodes.put(407, "Proxy Authentication Required");
		 statCodes.put(408, "Request Timeout");
		 statCodes.put(500, "Internal Server Error");
		 statCodes.put(501, "Not implemented");
		 statCodes.put(502, "Bad gateway");
		 statCodes.put(503, "Service Unavailable");
		 statCodes.put(504, "Gateway Timeout");
		 statCodes.put(505, "HTTP Version Not Supported");
		 statCodes.put(506, "Variant also negotiates");
		 statCodes.put(507, "Insufficient Storage");
		 statCodes.put(508, "Loop Detected");
		 statCodes.put(510, "Not extended");
		 statCodes.put(511, "Network Authentication Required");
		 System.setProperty("jsse.enableSNIExtension", "false");
		 String crawlStorageFolder = "D:\\Crawled Data";
		 int numberOfCrawlers = 7;
		 int maxDepthOfCrawling = 16;
		 int maxPagesToFetch = 10000;
		 int maxDownloadSize = 29999999;
		 //String[] crawlDomains = {"http://www.foxnews.com/"};
		 boolean includeBinaryContent = true;
		 
		 
		 CrawlConfig config = new CrawlConfig();
		 config.setCrawlStorageFolder(crawlStorageFolder);
		 config.setMaxPagesToFetch(maxPagesToFetch);
		 config.setConnectionTimeout(10000);
		 config.setPolitenessDelay(500);
		 config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		 config.setMaxDownloadSize(maxDownloadSize);
		 config.setIncludeBinaryContentInCrawling(includeBinaryContent);
		 
		 /*
		 * Instantiate the controller for this crawl.
		 */
		 PageFetcher pageFetcher = new PageFetcher(config);
		 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		 /*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		 controller.addSeed("http://abcnews.go.com");
		 
		 //for(String domain: crawlDomains){
		//	 controller.addSeed(domain);
		 //}
		 
		 MyCrawler.configure(crawlStorageFolder);
		 /*
		 * Start the crawl. This is a blocking operation, meaning that your code
		7
		 * will reach the line after this only when crawling is finished.
		 */
		 controller.start(MyCrawler.class, numberOfCrawlers);
		 
		 List<Object> dataList = controller.getCrawlersLocalData();
		 List<CrawledData> crawledDataList = new ArrayList<CrawledData>();
		 for(Object d : dataList){
			 crawledDataList.add((CrawledData)d);
		 }
		 
		 FileWriter fetchFile = new FileWriter("fetch.csv");
		 fetchFile.append("URL,StatusCode\n");
		 fetchFile.flush();
		 fetchFile.close();
		 
		 FileWriter visitedFile = new FileWriter("visit.csv");
		 visitedFile.append("URL,Size,Outlinks,Content-Type\n");
		 visitedFile.flush();
		 visitedFile.close();
		 
		 FileWriter urlFile = new FileWriter("urls.csv");
		 urlFile.append("URL,Location\n");
		 urlFile.flush();
		 urlFile.close();
		 
		 fetchFile = new FileWriter("fetch.csv", true);
		 visitedFile = new FileWriter("visit.csv", true);
		 urlFile = new FileWriter("urls.csv", true);
		 FileWriter report = new FileWriter("CrawlerReport.txt");
		 int fetchesAttempted = 0;
		 int fetchesSucceeded = 0;
		 int fetchesAborted = 0;
		 int fetchesFailed = 0;
		 int totalUrls = 0;
		 int uniqueURLInWebsite = 0;
		 int uniqueURLOutsideWebsite = 0;
		 //int uniqueURLOutsideUSC = 0;
		 
		 int size1 = 0;
		 int size2 = 0;
		 int size3 = 0;
		 int size4 = 0;
		 int size5 = 0;
		 int typeText = 0;
		 int typeGif = 0;
		 int typePng = 0;
		 int typePdf = 0;
		 int typeJpeg = 0;
		 int typeDoc = 0;
		 
		 Set<String> uniqueURLInWebsiteSet = new HashSet<String>();		 
		 Set<String> uniqueURLOutsideWebsiteSet = new HashSet<String>();
		 //Set<String> uniqueURLOutsideUSCSet = new HashSet<String>();
		 
		 for(CrawledData crawledData : crawledDataList){
			 List<FetchData> fetchDataList = crawledData.getFetchDataList();
			 for(FetchData fetchData : fetchDataList){
				 fetchFile.append(fetchData.getUrl() + "," + fetchData.getHTTPStatusCode() + "\n");
			 }
			 
			 List<VisitedData> visitedDataList = crawledData.getVisitedDataList();
			 for(VisitedData visitedData : visitedDataList){
				 visitedFile.append(visitedData.getUrl() + "," + visitedData.getContentLength() + "," + visitedData.getOutlinkSize() + "," + visitedData.getType() + "\n");
			 }
			 
			 List<UrlData> urlDataList = crawledData.getUrlDataList();
			 for(UrlData urlData : urlDataList){
				 urlFile.append(urlData.getUrlProcessed() + "," + getLocationString(urlData.getLocation()) + "\n");
			 }
			
			fetchesAttempted += crawledData.getFetchesAttempted();
			fetchesSucceeded += crawledData.getFetchesSucceeded();
			fetchesAborted += crawledData.getFetchesAborted();
			fetchesFailed += crawledData.getFetchesFailed();
			totalUrls+= crawledData.getTotalUrls();
			uniqueURLInWebsiteSet.addAll(crawledData.getUniqueURLInWebsite());
			uniqueURLOutsideWebsiteSet.addAll(crawledData.getUniqueURLOutsideWebsite());
			//uniqueURLOutsideUSCSet.addAll(crawledData.getUniqueURLOutsideUSC());
			/*if(MyCrawler.status.containsKey(200))
			status200 += MyCrawler.status.get(200);
			else if(MyCrawler.status.containsKey(301))
			status301 += MyCrawler.status.get(301);
			else if(MyCrawler.status.containsKey(401))
			status401 += MyCrawler.status.get(401);
			else if(MyCrawler.status.containsKey(403))
			status403 += MyCrawler.status.get(403);
			else if(MyCrawler.status.containsKey(404))
			status404 += MyCrawler.status.get(404);
			else if(MyCrawler.status.containsKey(500))
			status500 += MyCrawler.status.get(500);
			else if(MyCrawler.status.containsKey(501))
			status501 += MyCrawler.status.get(501);
			
			else if(MyCrawler.status.containsKey(503))
			status503 += MyCrawler.status.get(503);*/
			size1 += crawledData.getSize1();
			size2 += crawledData.getSize2();
			size3 += crawledData.getSize3();
			size4 += crawledData.getSize4();
			size5 += crawledData.getSize5();
			typeText += crawledData.getTypeText();
			typeGif += crawledData.getTypeGif();
			typePng += crawledData.getTypePng();
			typePdf += crawledData.getTypePdf();
			typeJpeg += crawledData.getTypeJpeg();
			typeDoc += crawledData.getTypeDoc();
		 }
		 fetchFile.close();
		 visitedFile.close();
		 urlFile.close();
		 
		 uniqueURLInWebsite = uniqueURLInWebsiteSet.size();
		 uniqueURLOutsideWebsite = uniqueURLOutsideWebsiteSet.size();
		 //uniqueURLOutsideUSC = uniqueURLOutsideUSCSet.size();
		 
		 report.append("Name: Ashwin Hariharan\n");
		 report.append("USC ID: 3981903071\n");
		 report.append("News site crawled: Fox News\n");
		 
		 report.append("\nFetch Statistics\n");
		 report.append("================\n");
		 report.append("# fetches attempted: " + fetchesAttempted + "\n");
		 report.append("# fetches succeeded: " + fetchesSucceeded + "\n");
		 report.append("# fetches aborted: " + fetchesAborted + "\n");
		 report.append("# fetches failed: " + fetchesFailed + "\n");
		 
		 report.append("\nOutgoing URLs:\n");
		 report.append("================\n");
		 report.append("Total URLs extracted: " + totalUrls + "\n");
		 report.append("# unique URLs extracted: " + (uniqueURLInWebsite+uniqueURLOutsideWebsite) + "\n");
		 report.append("# unique URLs within News Site: " + uniqueURLInWebsite + "\n");
		 report.append("# unique URLs outside News Site: " + uniqueURLOutsideWebsite + "\n");
		 //report.append("# unique URLs outside USC: " + uniqueURLOutsideUSC + "\n");
		 
		 report.append("\nStatus Codes:\n");
		 report.append("================\n");
		 
		 /*report.append("200 OK: " + status200 + "\n");
		 report.append("301 Moved Permanently: " + status301 + "\n");
		 report.append("401 Unauthorized: " + status401 + "\n");
		 report.append("403 Forbidden: " + status403 + "\n");
		 report.append("404 Not Found: " + status404 + "\n");
		 report.append("500 Internal Server Error: " + status500 + "\n");
		 report.append("501 Not Implemented: " + status501 + "\n");
		 report.append("503 Service Unavailable: " + status503 + "\n");*/
		 report.append("//Only encountered status codes displayed in report\n");
		 for(Integer key : MyCrawler.status.keySet())
		 {
			 report.append(key + " " + statCodes.get(key) + ": " + MyCrawler.status.get(key) + "\n");
		 }
		 
		 report.append("\nFile Sizes:\n");
		 report.append("================\n");
		 report.append("< 1KB: " + size1 + "\n");
		 report.append("1KB ~ <10KB: " + size2 + "\n");
		 report.append("10KB ~ <100KB: " + size3 + "\n");
		 report.append("100KB ~ <1MB: " + size4 + "\n");
		 report.append(">= 1MB: " + size5 + "\n");
		 
		 report.append("\nContent Types:\n");
		 report.append("================\n");
		 report.append("text/html: " + typeText + "\n");
		 report.append("image/gif: " + typeGif + "\n");
		 report.append("image/jpeg: " + typeJpeg + "\n");
		 report.append("image/png: " + typePng + "\n");
		 report.append("application/pdf: " + typePdf + "\n");
		 report.append("doc: " + typeDoc + "\n");
		 report.close();
	}

	private static String getLocationString(Location location) {
		if(location == Location.OK){
			return "OK";
		}
		
		return "N_OK";
	}
		
}
