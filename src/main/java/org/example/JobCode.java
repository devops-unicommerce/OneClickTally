package org.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JobCode {
    private String jobCodeValue;

    public String getJobCodeValue() {
        return jobCodeValue;
    }

    public void setJobCodeValue(String jobCodeValue) {
        this.jobCodeValue = jobCodeValue;
    }


    public JobCode(ReadFile readResouceFile) {
        String xml;

        if (readResouceFile.getExportName() == "Tally Reco report new") {
            xml = getXML(readResouceFile, getPartialColumnList());
        } else {
            xml = getXML(readResouceFile, getEntireColumnList());
        }

        String jobCodeFromResponse = callSoapService(readResouceFile.getUrl(), xml);
        this.setJobCodeValue(jobCodeFromResponse);
    }


    static String callSoapService(String soapUrl, String soapRequest) {
        try {
            String url = soapUrl;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(soapRequest);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            System.out.println(responseStatus);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String finalvalue = response.toString();
            finalvalue = finalvalue.substring(finalvalue.indexOf("<JobCode>") + 9, finalvalue.indexOf("</JobCode>"));
            System.out.println("Job Code value is : " + finalvalue);

            return finalvalue;
        } catch (Exception e) {
            System.out.println("Exception occured while executing the script " + e.getMessage());
            return null;
        }
    }

    static String getXML(ReadFile readResouceFile, String columnList) {

        String xml = "<soapenv:Envelope xmlns:ser=\"http://uniware.unicommerce.com/services/\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" +
                "<soapenv:Header>\r\n" +
                "<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\r\n" +
                "<wsse:UsernameToken wsu:Id=\"UsernameToken-098567B6EF76ED49BF16814600696921\">\r\n" +
                "<wsse:Username>" + readResouceFile.getUserName() + "</wsse:Username>\r\n" +
                "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" + readResouceFile.getApiKey() + "</wsse:Password>\r\n" +
                "<wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">" + "r0HNHig+iEyWJ5bkAdvw/g==" + "</wsse:Nonce>\r\n" +
                "</wsse:UsernameToken>\r\n" +
                "</wsse:Security>\r\n" +
                "</soapenv:Header>\r\n" +
                "<soapenv:Body>\r\n" +
                "<ser:CreateExportJobRequest>\r\n" +
                "<ser:ExportJobTypeName>" + readResouceFile.getExportName() + "</ser:ExportJobTypeName>\r\n" +
                "<ser:ExportColumns>\r\n" +
                columnList +
                "</ser:ExportColumns>\r\n" +
                "<ser:ExportFilters>\r\n" +
                "<ser:ExportFilter id=\"addedOn\">\r\n" +
                "<ser:DateRange>\r\n" +
                "<ser:Start>" + readResouceFile.getStart_Date() + "T00:00:00+05:30</ser:Start>\r\n" +
                "<ser:End>" + readResouceFile.getEnd_Date() + "T23:59:59+05:30</ser:End>\r\n" +
                "</ser:DateRange>\r\n" +
                "</ser:ExportFilter>\r\n" +
                "</ser:ExportFilters>\r\n" +
                "<ser:Frequency>1</ser:Frequency>\r\n" +
                "</ser:CreateExportJobRequest>\r\n" +
                "</soapenv:Body>\r\n" +
                "</soapenv:Envelope>";

        return xml;

    }


    static String getEntireColumnList() {
        String entireColumnList = "<ser:ExportColumn>invoiceDate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>saleOrderCode</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>channelName</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>channelLedgerName</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>productCode</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>productSKU</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>QTY</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>unitPrice</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>Currency</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>currencyConversionRate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>total</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>customerName</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressName</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressLine1</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressLine2</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressCity</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressState</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressCountry</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressPincode</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingAddressPhone</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>shippingProvider</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>trackingNumber</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>sales</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>salesLedger</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>cgst</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>cgstRate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>sgst</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>sgstRate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>igst</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>igstRate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>utgst</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>utgstRate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>cess</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>cessRate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>OtherCharges</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>OtherChargesLedger</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>OtherCharges1</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>OtherChargesLedger1</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>Servicetax</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>ServicetaxLedger</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>discountLedger</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>discountAmount</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>imei</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>godDown</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>dispatchdate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>narration</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>entity</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>voucherTypeName</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>tin</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>original</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>original1</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>channelInvoiceDate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>channelState</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>channelPartyGSTIN</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>customerGSTIN</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>billingPartyCode</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>taxLedgerA</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>taxLedgerB</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>taxVerification</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>gstregistrationtype</ser:ExportColumn>\r\n";

        return entireColumnList;
    }

    static String getPartialColumnList() {
        String partialColumn = "<ser:ExportColumn>orderDate</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>channelCode</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>settledValue</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>invoiceItemOrderStatus</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>invoiceCode</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>channelSaleOrderCode</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>reconciliationIdentifier</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>orderItemValue</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalShippingCharge</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalCommission</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalShippingFee</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalFixedFee</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalReverseShippingFee</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalChannelPenalty</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalRewards</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalDiscounts</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalOtherIncentive</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalAdditionalFee</ser:ExportColumn>\r\n" +
                "<ser:ExportColumn>totalTax</ser:ExportColumn>\r\n";

        return partialColumn;
    }


}
