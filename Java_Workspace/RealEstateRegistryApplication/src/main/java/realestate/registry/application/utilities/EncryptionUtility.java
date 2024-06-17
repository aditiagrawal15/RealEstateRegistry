package realestate.registry.application.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import realestate.registry.application.beans.PropertyOwnership;
import realestate.registry.application.beans.RegistryUser;
import realestate.registry.application.beans.WorkFlow;

public class EncryptionUtility {
	   private static MessageDigest md;

	   public static String cryptWithMD5(String pass){
	    try {
	        md = MessageDigest.getInstance("MD5");
	        byte[] passBytes = pass.getBytes();
	        md.reset();
	        byte[] digested = md.digest(passBytes);
	        StringBuffer sb = new StringBuffer();
	        for(int i=0;i<digested.length;i++){
	            sb.append(Integer.toHexString(0xff & digested[i]));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException ex) {
	        //Logger.getLogger(EncryptionUtility.class.getName()).log(Level.SEVERE, null, ex);
	    }
	        return null;


	   }
	   
	   public static String getTimestamp() {
		   return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
	   }
	   
	   public static ByteArrayInputStream generateSaleAgreementPDF(String purchaserName, String sellerName, String registrarName, String propertyValue) throws DocumentException, MalformedURLException, IOException, URISyntaxException
		{
		   String content = "\n"
		   		+ "\n                               SALE AGREEMENT"
		   		+ "\n"
		   		+ "\n"
		   		+ "This Agreement to sell is made on "+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH) +  " day of "+ (Calendar.getInstance().get(Calendar.MONTH)+1)+" month, year "+(Calendar.getInstance().get(Calendar.YEAR)) +"\n"
		   		+ "By "+sellerName.toUpperCase() +" (hereinafter called the seller) of the first party.\n"
		   		+ "AND\n"
		   		+ "Purchaser full name "+purchaserName.toUpperCase()+" (herein after called the purchaser) of the second party.\n"
		   		+ "The absolute owner in possession is the first party.\n"
		   		+ "The agreed property is being sold by the first party with all the rights, and with consent of all heirs,to the"
		   		+ " second party who has agreed to buy this property for a total consideration of "
		   		+ "-(" + propertyValue + " only) on these terms and conditions :-\n"
		   		+ "1.The first party have given assurance to the second party that the said property is not in mortgage,not "
		   		+ "attached in any bail as surety in any court, or financial institution or court nor any agreement to sell "
		   		+ "has been made with any other party prior to this agreement.\n"
		   		+ "2. The buyer has paid to the seller the total sale consideration of "
		   		+ "("+ propertyValue + " Only} as a full and final payment against the said "
		   		+ "property/plot, for which the seller /allottee/owner hereby acknowledge the receipt in the presence "
		   		+ "of witnesses. There is no amount due as balance against the said property/land/plot.\n"
		   		+ "3. The agreed property/plot is free from all sorts of burdens. The property/plot does not have any "
		   		+ "charges over it like, sale, gift,mortgage,lease, lien,attachment,demands,etc.\n"
		   		+ "4. The seller shall transfer the plot/property in the name of seller or his nominees and fulfill all "
		   		+ "requirements of concerned authorities.\n"
		   		+ "5. The seller should not have any objection if the buyer decides to sell the property/plot to any other "
		   		+ "person.\n"
		   		+ "6. In case the seller backs out from transferring the property and fulfilling the legal forma lities, then the "
		   		+ "buyer has complete right on the said property to get it transferred in his name with the help of court of "
		   		+ "law under the performance of specific relief act. In that case the seller is responsible to pay the cost and "
		   		+ "consequences , damages etc.\n"
		   		+ "\n"
		   		+ "\n"
		   		+ "This legal document is signed by both the parties in the presence of witnesses at on "
		   		+ "the day month and year first mentioned above, in the presence of marginal witnesses.\n"
		   		+ "Registrar Name : " + registrarName.toUpperCase() + "\n"
		   		+ "First party/seller : " + sellerName.toUpperCase() + "\n"
		   		+ "Second party/purchaser : "+purchaserName.toUpperCase()+"\n"
		   		+ "\n"
		   		+ "";

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4, 50, 50, 5 , 5);
			PdfWriter.getInstance(document, out);
			document.open();
			//Write PDF
			Path path = Paths.get(ClassLoader.getSystemResource("download.jfif").toURI());
			Image img = Image.getInstance(path.toAbsolutePath().toString());
			img.scalePercent(35, 35);
			img.setIndentationLeft(200);
			document.add(img);
			Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
//			Chunk chunk = new Chunk("\n  Sale Agreement", font);
//			Chunk chunk1 = new Chunk("Hello World", font);
			Paragraph para = new Paragraph(content,font);
//			document.add(chunk);
			document.add(para);
			document.close();


			return new ByteArrayInputStream(out.toByteArray());
		}
	   
	   public static ByteArrayInputStream generateRegistryCertificate(WorkFlow  workflow) throws DocumentException, URISyntaxException, MalformedURLException, IOException {
		   RegistryUser seller = workflow.getSellerApprover();
		   RegistryUser purchaser = workflow.getRaisedBy();
		   PropertyOwnership property = workflow.getRegistry().getPropertyFk();
		   String content = "                             SALE DEED\n"
		   		+ "This SALE DEED is made and executed on this " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) +   " of MONTH "+ (Calendar.getInstance().get(Calendar.MONTH)+1)+"  YEAR "+(Calendar.getInstance().get(Calendar.YEAR)) +"\n"
		   		+ "BETWEEN \n"
		   		+ workflow.getSellerApproverByNameWithAadhar() + ", holding PAN "+seller.getPanNumber()+ " by Nationality Indian, residing at "+ seller.getAddress()+ " hereinafter called the \"SELLER\" (which expression shall mean and include his legal heirs, successors, successors-in-interest, executors, administrators, legal representatives and assigns) of the ONE PART.\n"
		   		+ "AND\n"
		   		+ workflow.getRaisedByNameWithAadhar() + " by Nationality Indian,  holding PAN "+purchaser.getPanNumber()+ " residing at " +purchaser.getAddress()+ " hereinafter called the \"PURCHASER\" (which expression shall mean and include his legal heirs, successors, successors-in-interest, executors, administrators, legal representatives and assigns) of the  OTHER PART.\n"
		   		+ "The SELLER and the PURCHASER are hereinafter referred collectively as parties and individually as party.\n"
		   		+ "WHEREAS the SELLER is the absolute owner, in possession and enjoyment of the piece and parcel of land measuring about "+ property.getPropertyArea() +" sq.ft., lying and situated in Plot Number "+property.getPlotNo() +", under Police Station "+ property.getPoliceStation()+" , Registration in the district of "+ property.getDistrict()+", more fully and particularly described in the schedule here under written and hereafter referred to as the \"SCHEDULE PROPERTY”.\n"
		   		+ "ANDWHEREAS the SCHEDULE PROPERTY was the self acquired property of deceased father of the SELLER.\n"
		   		+ "ANDWHEREAS the SELLER herein, as the only legal heirs of the deceased have become the absolute owner of the SCHEDULE PROPERTY since the death of his father on and he has been enjoying the same with absolute right, title and interest since then and he has clear and marketable title to the SCHEDULE PROPERTY.\n"
		   		+ "ANDWHEREAS the SELLER being in need of funds to meet his personal commitments and family expenses have decided to sell the SCHEDULE PROPERTY and the PURCHASER has agreed to purchase the same.\n"
		   		+ "ANDWHEREAS the SELLER agreed to sell, convey and transfer the SCHEDULE PROPERTY to the PURCHASER for a total consideration of "+ property.getPropertyValueWithComma() +" only and the PURCHASER herein agreed to purchase the same for the aforesaid consideration and to that effect the parties entered into an agreement on the "+ workflow.getUpdatedDate() +"\n"
		   		+ "NOW THIS DEED OF SALE WITNESSETH:\n"
		   		+ "THAT in pursuance of the aforesaid agreement and in consideration of a sum of Rs "+ property.getPropertyValueWithComma() + " only received by the SELLER in cash/cheque/bankdraft and upon receipt of the said entire consideration of Rs "+ property.getPropertyValueWithComma() + " only (the SELLER doth hereby admit, acknowledge, acquit, release and discharge the PURCHASER from making further payment thereof) the SELLER doth hereby sells, conveys, transfers, and assigns unto and to the use of the PURCHASER the SCHEDULE PROPERTY together with the water ways, easements, advantages and appurtenances, and all estate, rights, title and interest of the SELLER to and upon the SCHEDULE PROPERTY TO HAVE AND TO HOLD the SCHEDULE PROPERTY hereby conveyed unto the PURCHASER absolutely and forever.\n"
		   		+ "THAT THE SELLER DOTH HEREBY COVENANT WITH THE PURCHASER AS FOLLOWS:\n"
		   		+ "That the SCHEDULE PROPERTY shall be quietly and peacefully entered into and held and enjoyed by the PURCHASER without any interference, interruption, or disturbance from the SELLER or any person claiming through or under him.\n"
		   		+ "That the SELLER have absolute  right, title and full power to sell, convey and transfer unto the PURCHASER by way of absolute sale and that the SELLER have not done anything or knowingly suffered anything whereby their right and power to sell and convey the SCHEDULE PROPERTY to the PURCHASER is diminished. \n"
		   		+ "That the property is not subjected to any encumbrances, mortgages, charges, lien, attachments, claim, demand, acquisition proceedings by Government or any kind whatsoever and should thereby and the SELLER shall discharge the same from and out of his own fund and keep the PURCHASER indemnified.\n"
		   		+ "That the SELLER hereby declares with the PURCHASER that the SELLER have paid all the taxes, rates and other outgoings due to local bodies, revenue, urban and other authorities in respect of the SCHEDULE PROPERTY up to the date of execution of this sale deed and the PURCHASER shall bear and pay the same hereafter.  If any arrears are found due for the earlier period, the same shall be discharged/borne by the SELLER.\n"
		   		+ "That the SELLER have handed over the vacant possession of the SCHEDULE PROPERTY to the PURCHASER on "+ workflow.getUpdatedDate() +" and delivered the connected original title document in respect of the SCHEDULE PROPERTY hereby conveyed on the date of execution of these presents.\n"
		   		+ "That the SELLER will at all times and at the cost of the PURCHASER execute, register or cause to be done, all such acts and deeds for perfecting the title to the PURCHASER in the property hereby sold and conveyed herein.\n"
		   		+ "That the SELLER do hereby covenants and assures that the PURCHASER is entitled to have mutation of his name in all public records, local body and also obtain all documents in the name of the PURCHASER and undertakes to execute any deed in this respect.\n"
		   		+ "SCHEDULE  OF PROPERTY\n"
		   		+ "All that piece and parcel of land measuring about "+ property.getPropertyArea() +"sq. ft., lying and situated in Plot Number "+property.getPlotNo()+", under Police Station "+property.getPoliceStation()+", Registration in the district of "+property.getPoliceStation()+".\n"
		   		+ "IN WITNESS WHEREOF the SELLER and the PURCHASER have set their signatures on the day month and year first above written.\n"
		   		+ "\n"
		   		+ "\n"
		   		+ "Registrar Name : " + workflow.getRegistrarApproverByNameWithAadhar() + "\n"
		   		+ "First party/seller : " + workflow.getSellerApproverByNameWithAadhar().toUpperCase() + "\n"
		   		+ "Second party/purchaser : "+workflow.getRaisedByNameWithAadhar().toUpperCase()+"\n"
		   		+ " \n"
		   		+ "";
		   
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4, 50, 50, 5 , 5);
			PdfWriter.getInstance(document, out);
			document.open();
			
			Path path = Paths.get(ClassLoader.getSystemResource("stamp.jfif").toURI());
			Image img = Image.getInstance(path.toAbsolutePath().toString());
			img.scalePercent(150, 150);
			document.add(img);
			Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
			Paragraph para = new Paragraph(content,font);
			
			document.add(para);
			document.close();


			return new ByteArrayInputStream(out.toByteArray());
			
	   }
	}
