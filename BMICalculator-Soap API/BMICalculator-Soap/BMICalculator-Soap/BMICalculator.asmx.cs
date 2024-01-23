using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace BMICalculator_Soap
{
    /// <summary>
    /// Summary description for BMICalculator
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class BMICalculator : System.Web.Services.WebService
    {

        [WebMethod]
        public BMI MyHealth(int height, int weight)
        {

            BMI b = new BMI();
            b.bmi = b.myBMI(height, weight);
            if (b.bmi < 18)
            {
                b.risk = "You are underweight";
            }

            if (b.bmi < 25 && b.bmi >= 18)
            {
                b.risk = "Your weight is normal";
            }
            if (b.bmi < 30 && b.bmi >= 25)
            {
                b.risk = "You are pre - obese";
            }
            else if (b.bmi > 30)
            {
                b.risk = "You are obese";
            }

            b.more.SetValue("https://www.cdc.gov/healthyweight/assessing/bmi/index.html", 0);
            b.more.SetValue("https://www.nhlbi.nih.gov/health/educational/lose_wt/index.htm", 1);
            b.more.SetValue("https://www.ucsfhealth.org/education/body_mass_index_tool/", 2);

            return b;

        }

        public class BMI
        {

            public double bmi { get; set; }

            public String risk { get; set; }

            public String[] more { get; set; }

            public BMI()
            {
                more = new String[3];
            }
            public double myBMI(int height, int weight)
            {
                return (Convert.ToDouble(weight) / (height * height)) * 703;
            }
        }
    }

}
