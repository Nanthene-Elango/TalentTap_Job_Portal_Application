const companies = {
    1: {
        company_name: "StarCafe",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Hospitality",
        company_size: "500-1000 employees",
        website_url: "https://www.starcafe.com",
        founded_at: "1971",
        About: "StarCafe is a leading hospitality company dedicated to providing exceptional coffee and dining experiences worldwide.",
        isverified: true,
        rating: 3.7,
        reviews: 5324,
        jobs: [
            {
                job_id: 1,
                employer_id: 101,
                job_Role: "Barista",
                job_type: "Full-time",
                job_category: "Service",
                description: "Prepare and serve coffee beverages to customers.",
                location_id: 1,
                duration: null,
                stipend: null,
                salary_range_id: 1,
                years_of_experience: "0-2 years",
                openings: 5,
                posted_date: "2025-03-21T10:00:00",
                deadline: "2025-04-01T23:59:59",
                applications: 12,
                skills: ["Customer Service", "Coffee Preparation", "Teamwork"]
            },
            {
                job_id: 2,
                employer_id: 101,
                job_Role: "Store Manager",
                job_type: "Full-time",
                job_category: "Management",
                description: "Manage store operations and staff.",
                location_id: 1,
                duration: null,
                stipend: null,
                salary_range_id: 2,
                years_of_experience: "3-5 years",
                openings: 2,
                posted_date: "2025-03-20T09:00:00",
                deadline: "2025-04-05T23:59:59",
                applications: 8,
                skills: ["Management", "Leadership", "Operations"]
            },
            {
                job_id: 3,
                employer_id: 101,
                job_Role: "Marketing Intern",
                job_type: "Internship",
                job_category: "Marketing",
                description: "Assist with marketing campaigns and social media.",
                location_id: 1,
                duration: 6,
                stipend: 500,
                salary_range_id: null,
                years_of_experience: "0 years",
                openings: 3,
                posted_date: "2025-03-18T14:00:00",
                deadline: "2025-03-31T23:59:59",
                applications: 15,
                skills: ["Marketing", "Social Media", "Campaigns"]
            }
        ]
    },
    2: {
        company_name: "EduPro",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Education",
        company_size: "200-500 employees",
        website_url: "https://www.edupro.com",
        founded_at: "1977",
        About: "EduPro is dedicated to transforming education through innovative learning solutions.",
        isverified: true,
        rating: 4.1,
        reviews: 3200,
        jobs: [
            {
                job_id: 4,
                employer_id: 102,
                job_Role: "Curriculum Developer",
                job_type: "Full-time",
                job_category: "Education",
                description: "Design and develop educational content and curricula.",
                location_id: 2,
                duration: null,
                stipend: null,
                salary_range_id: 3,
                years_of_experience: "2-4 years",
                openings: 3,
                posted_date: "2025-03-22T08:00:00",
                deadline: "2025-04-10T23:59:59",
                applications: 10,
                skills: ["Curriculum Design", "Education", "Content Creation"]
            }
        ]
    },
    3: {
        company_name: "TechTrend",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Technology",
        company_size: "1000+ employees",
        website_url: "https://www.techtrend.com",
        founded_at: "2019",
        About: "TechTrend is a cutting-edge technology company focused on innovative software solutions.",
        isverified: true,
        rating: 4.5,
        reviews: 7800,
        jobs: [
            {
                job_id: 5,
                employer_id: 103,
                job_Role: "Software Engineer",
                job_type: "Full-time",
                job_category: "Engineering",
                description: "Develop and maintain software applications.",
                location_id: 3,
                duration: null,
                stipend: null,
                salary_range_id: 4,
                years_of_experience: "1-3 years",
                openings: 5,
                posted_date: "2025-03-19T12:00:00",
                deadline: "2025-04-15T23:59:59",
                applications: 20,
                skills: ["JavaScript", "React", "Node.js"]
            }
        ]
    },
    4: {
        company_name: "FinGlobal",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Finance",
        company_size: "500-1000 employees",
        website_url: "https://www.finglobal.com",
        founded_at: "1995",
        About: "FinGlobal provides financial services and solutions globally.",
        isverified: true,
        rating: 3.9,
        reviews: 4500,
        jobs: [
            {
                job_id: 6,
                employer_id: 104,
                job_Role: "Financial Analyst",
                job_type: "Full-time",
                job_category: "Finance",
                description: "Analyze financial data and provide insights.",
                location_id: 4,
                duration: null,
                stipend: null,
                salary_range_id: 5,
                years_of_experience: "2-5 years",
                openings: 4,
                posted_date: "2025-03-23T09:00:00",
                deadline: "2025-04-20T23:59:59",
                applications: 18,
                skills: ["Financial Analysis", "Excel", "Data Interpretation"]
            }
        ]
    },
    5: {
        company_name: "HealthPlus",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Healthcare",
        company_size: "500-1000 employees",
        website_url: "https://www.healthplus.com",
        founded_at: "1985",
        About: "HealthPlus is committed to improving healthcare services and patient care.",
        isverified: true,
        rating: 4.0,
        reviews: 3800,
        jobs: [
            {
                job_id: 7,
                employer_id: 105,
                job_Role: "Nurse Practitioner",
                job_type: "Full-time",
                job_category: "Healthcare",
                description: "Provide patient care and support in a clinical setting.",
                location_id: 5,
                duration: null,
                stipend: null,
                salary_range_id: 6,
                years_of_experience: "3-5 years",
                openings: 6,
                posted_date: "2025-03-24T10:00:00",
                deadline: "2025-04-25T23:59:59",
                applications: 25,
                skills: ["Patient Care", "Clinical Skills", "Communication"]
            }
        ]
    },
    6: {
        company_name: "GreenEnergy",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Energy",
        company_size: "200-500 employees",
        website_url: "https://www.greenenergy.com",
        founded_at: "2010",
        About: "GreenEnergy focuses on sustainable energy solutions for a better future.",
        isverified: true,
        rating: 4.2,
        reviews: 2900,
        jobs: [
            {
                job_id: 8,
                employer_id: 106,
                job_Role: "Energy Analyst",
                job_type: "Full-time",
                job_category: "Energy",
                description: "Analyze energy consumption and recommend improvements.",
                location_id: 6,
                duration: null,
                stipend: null,
                salary_range_id: 7,
                years_of_experience: "2-4 years",
                openings: 3,
                posted_date: "2025-03-25T11:00:00",
                deadline: "2025-04-30T23:59:59",
                applications: 15,
                skills: ["Energy Analysis", "Sustainability", "Data Analysis"]
            }
        ]
    },
    7: {
        company_name: "InnovaTech",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Technology",
        company_size: "1000+ employees",
        website_url: "https://www.innovatech.com",
        founded_at: "2015",
        About: "InnovaTech drives innovation through technology and research.",
        isverified: true,
        rating: 4.3,
        reviews: 6200,
        jobs: [
            {
                job_id: 9,
                employer_id: 107,
                job_Role: "Data Scientist",
                job_type: "Full-time",
                job_category: "Technology",
                description: "Analyze data to provide actionable insights.",
                location_id: 7,
                duration: null,
                stipend: null,
                salary_range_id: 8,
                years_of_experience: "3-5 years",
                openings: 4,
                posted_date: "2025-03-26T12:00:00",
                deadline: "2025-05-01T23:59:59",
                applications: 22,
                skills: ["Python", "Machine Learning", "Statistics"]
            }
        ]
    },
    8: {
        company_name: "EduLearn",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Education",
        company_size: "200-500 employees",
        website_url: "https://www.edulearn.com",
        founded_at: "2000",
        About: "EduLearn provides online learning platforms for students worldwide.",
        isverified: true,
        rating: 3.8,
        reviews: 3100,
        jobs: [
            {
                job_id: 10,
                employer_id: 108,
                job_Role: "Instructional Designer",
                job_type: "Full-time",
                job_category: "Education",
                description: "Design online courses and learning materials.",
                location_id: 8,
                duration: null,
                stipend: null,
                salary_range_id: 3,
                years_of_experience: "2-4 years",
                openings: 2,
                posted_date: "2025-03-27T13:00:00",
                deadline: "2025-05-05T23:59:59",
                applications: 12,
                skills: ["Instructional Design", "E-Learning", "Content Development"]
            }
        ]
    },
    9: {
        company_name: "FinSecure",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Finance",
        company_size: "500-1000 employees",
        website_url: "https://www.finsecure.com",
        founded_at: "1990",
        About: "FinSecure offers secure financial services and insurance solutions.",
        isverified: true,
        rating: 4.0,
        reviews: 4200,
        jobs: [
            {
                job_id: 11,
                employer_id: 109,
                job_Role: "Risk Analyst",
                job_type: "Full-time",
                job_category: "Finance",
                description: "Assess financial risks and develop mitigation strategies.",
                location_id: 9,
                duration: null,
                stipend: null,
                salary_range_id: 5,
                years_of_experience: "3-5 years",
                openings: 3,
                posted_date: "2025-03-28T14:00:00",
                deadline: "2025-05-10T23:59:59",
                applications: 18,
                skills: ["Risk Assessment", "Finance", "Analytical Skills"]
            }
        ]
    },
    10: {
        company_name: "GlobalSoft",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Software",
        company_size: "1000+ employees",
        website_url: "https://www.globalsoft.com",
        founded_at: "2005",
        About: "GlobalSoft develops software solutions for global enterprises.",
        isverified: true,
        rating: 4.4,
        reviews: 5800,
        jobs: [
            {
                job_id: 12,
                employer_id: 110,
                job_Role: "DevOps Engineer",
                job_type: "Full-time",
                job_category: "Engineering",
                description: "Manage CI/CD pipelines and infrastructure.",
                location_id: 10,
                duration: null,
                stipend: null,
                salary_range_id: 9,
                years_of_experience: "2-5 years",
                openings: 4,
                posted_date: "2025-03-29T15:00:00",
                deadline: "2025-05-15T23:59:59",
                applications: 20,
                skills: ["AWS", "Docker", "CI/CD"]
            }
        ]
    },
    11: {
        company_name: "CloudNine",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Technology",
        company_size: "200-500 employees",
        website_url: "https://www.cloudnine.com",
        founded_at: "2018",
        About: "CloudNine provides cloud computing solutions for businesses.",
        isverified: true,
        rating: 4.1,
        reviews: 3400,
        jobs: [
            {
                job_id: 13,
                employer_id: 111,
                job_Role: "Cloud Engineer",
                job_type: "Full-time",
                job_category: "Technology",
                description: "Design and manage cloud infrastructure.",
                location_id: 11,
                duration: null,
                stipend: null,
                salary_range_id: 8,
                years_of_experience: "2-4 years",
                openings: 3,
                posted_date: "2025-03-30T16:00:00",
                deadline: "2025-05-20T23:59:59",
                applications: 15,
                skills: ["Azure", "Cloud Computing", "Networking"]
            }
        ]
    },
    12: {
        company_name: "EnergyCore",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Energy",
        company_size: "200-500 employees",
        website_url: "https://www.energycore.com",
        founded_at: "2012",
        About: "EnergyCore focuses on renewable energy technologies.",
        isverified: true,
        rating: 4.0,
        reviews: 2700,
        jobs: [
            {
                job_id: 14,
                employer_id: 112,
                job_Role: "Renewable Energy Specialist",
                job_type: "Full-time",
                job_category: "Energy",
                description: "Develop renewable energy projects.",
                location_id: 12,
                duration: null,
                stipend: null,
                salary_range_id: 7,
                years_of_experience: "3-5 years",
                openings: 2,
                posted_date: "2025-03-31T17:00:00",
                deadline: "2025-05-25T23:59:59",
                applications: 10,
                skills: ["Renewable Energy", "Project Management", "Sustainability"]
            }
        ]
    },
    13: {
        company_name: "LearnWise",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Education",
        company_size: "200-500 employees",
        website_url: "https://www.learnwise.com",
        founded_at: "2008",
        About: "LearnWise offers educational tools and resources for teachers and students.",
        isverified: true,
        rating: 3.9,
        reviews: 3000,
        jobs: [
            {
                job_id: 15,
                employer_id: 113,
                job_Role: "Educational Consultant",
                job_type: "Full-time",
                job_category: "Education",
                description: "Provide guidance on educational strategies.",
                location_id: 13,
                duration: null,
                stipend: null,
                salary_range_id: 3,
                years_of_experience: "2-4 years",
                openings: 2,
                posted_date: "2025-04-01T18:00:00",
                deadline: "2025-05-30T23:59:59",
                applications: 8,
                skills: ["Education Consulting", "Teaching", "Strategy"]
            }
        ]
    },
    14: {
        company_name: "FinTechPro",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Finance",
        company_size: "500-1000 employees",
        website_url: "https://www.fintechpro.com",
        founded_at: "2016",
        About: "FinTechPro develops fintech solutions for modern banking.",
        isverified: true,
        rating: 4.2,
        reviews: 3900,
        jobs: [
            {
                job_id: 16,
                employer_id: 114,
                job_Role: "Blockchain Developer",
                job_type: "Full-time",
                job_category: "Technology",
                description: "Develop blockchain-based financial applications.",
                location_id: 14,
                duration: null,
                stipend: null,
                salary_range_id: 9,
                years_of_experience: "3-5 years",
                openings: 3,
                posted_date: "2025-04-02T19:00:00",
                deadline: "2025-06-01T23:59:59",
                applications: 12,
                skills: ["Blockchain", "Solidity", "Finance"]
            }
        ]
    },
    15: {
        company_name: "SoftPeak",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Software",
        company_size: "200-500 employees",
        website_url: "https://www.softpeak.com",
        founded_at: "2011",
        About: "SoftPeak provides software development services for various industries.",
        isverified: true,
        rating: 4.1,
        reviews: 3500,
        jobs: [
            {
                job_id: 17,
                employer_id: 115,
                job_Role: "Full Stack Developer",
                job_type: "Full-time",
                job_category: "Engineering",
                description: "Develop and maintain web applications.",
                location_id: 15,
                duration: null,
                stipend: null,
                salary_range_id: 8,
                years_of_experience: "2-4 years",
                openings: 4,
                posted_date: "2025-04-03T20:00:00",
                deadline: "2025-06-05T23:59:59",
                applications: 15,
                skills: ["JavaScript", "Python", "Django"]
            }
        ]
    },
    16: {
        company_name: "HealthWave",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Healthcare",
        company_size: "500-1000 employees",
        website_url: "https://www.healthwave.com",
        founded_at: "1998",
        About: "HealthWave innovates in healthcare technology and services.",
        isverified: true,
        rating: 4.0,
        reviews: 4000,
        jobs: [
            {
                job_id: 18,
                employer_id: 116,
                job_Role: "Medical Technologist",
                job_type: "Full-time",
                job_category: "Healthcare",
                description: "Conduct medical tests and analyze results.",
                location_id: 16,
                duration: null,
                stipend: null,
                salary_range_id: 6,
                years_of_experience: "3-5 years",
                openings: 5,
                posted_date: "2025-04-04T21:00:00",
                deadline: "2025-06-10T23:59:59",
                applications: 20,
                skills: ["Medical Testing", "Analysis", "Healthcare"]
            }
        ]
    },
    17: {
        company_name: "TechSpire",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Technology",
        company_size: "200-500 employees",
        website_url: "https://www.techspire.com",
        founded_at: "2020",
        About: "TechSpire is a startup focused on AI and machine learning solutions.",
        isverified: true,
        rating: 4.3,
        reviews: 2800,
        jobs: [
            {
                job_id: 19,
                employer_id: 117,
                job_Role: "AI Engineer",
                job_type: "Full-time",
                job_category: "Technology",
                description: "Develop AI models and algorithms.",
                location_id: 17,
                duration: null,
                stipend: null,
                salary_range_id: 9,
                years_of_experience: "2-5 years",
                openings: 3,
                posted_date: "2025-04-05T22:00:00",
                deadline: "2025-06-15T23:59:59",
                applications: 18,
                skills: ["AI", "Machine Learning", "Python"]
            }
        ]
    },
    18: {
        company_name: "EduSmart",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Education",
        company_size: "200-500 employees",
        website_url: "https://www.edusmart.com",
        founded_at: "2003",
        About: "EduSmart provides smart education solutions for schools.",
        isverified: true,
        rating: 3.8,
        reviews: 2900,
        jobs: [
            {
                job_id: 20,
                employer_id: 118,
                job_Role: "Education Technology Specialist",
                job_type: "Full-time",
                job_category: "Education",
                description: "Implement technology in educational settings.",
                location_id: 18,
                duration: null,
                stipend: null,
                salary_range_id: 3,
                years_of_experience: "2-4 years",
                openings: 2,
                posted_date: "2025-04-06T23:00:00",
                deadline: "2025-06-20T23:59:59",
                applications: 10,
                skills: ["EdTech", "Teaching", "Technology Integration"]
            }
        ]
    },
    19: {
        company_name: "FinTrust",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Finance",
        company_size: "500-1000 employees",
        website_url: "https://www.fintrust.com",
        founded_at: "1988",
        About: "FinTrust offers trusted financial advisory services.",
        isverified: true,
        rating: 4.1,
        reviews: 4100,
        jobs: [
            {
                job_id: 21,
                employer_id: 119,
                job_Role: "Financial Advisor",
                job_type: "Full-time",
                job_category: "Finance",
                description: "Provide financial advice to clients.",
                location_id: 19,
                duration: null,
                stipend: null,
                salary_range_id: 5,
                years_of_experience: "3-5 years",
                openings: 3,
                posted_date: "2025-04-07T09:00:00",
                deadline: "2025-06-25T23:59:59",
                applications: 15,
                skills: ["Financial Planning", "Client Relations", "Investment"]
            }
        ]
    },
    20: {
        company_name: "GlobalTech",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Software",
        company_size: "1000+ employees",
        website_url: "https://www.globaltech.com",
        founded_at: "2007",
        About: "GlobalTech delivers technology solutions for global markets.",
        isverified: true,
        rating: 4.2,
        reviews: 5500,
        jobs: [
            {
                job_id: 22,
                employer_id: 120,
                job_Role: "Cybersecurity Analyst",
                job_type: "Full-time",
                job_category: "Security",
                description: "Protect systems from cyber threats.",
                location_id: 20,
                duration: null,
                stipend: null,
                salary_range_id: 9,
                years_of_experience: "2-5 years",
                openings: 4,
                posted_date: "2025-04-08T10:00:00",
                deadline: "2025-06-30T23:59:59",
                applications: 20,
                skills: ["Cybersecurity", "Network Security", "Threat Analysis"]
            }
        ]
    },
    21: {
        company_name: "TechNova",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Technology",
        company_size: "1000+ employees",
        website_url: "https://www.technova.com",
        founded_at: "2010",
        About: "TechNova pioneers technology solutions for the future.",
        isverified: true,
        rating: 4.4,
        reviews: 6000,
        jobs: [
            {
                job_id: 23,
                employer_id: 121,
                job_Role: "Product Manager",
                job_type: "Full-time",
                job_category: "Management",
                description: "Oversee product development and strategy.",
                location_id: 4,
                duration: null,
                stipend: null,
                salary_range_id: 10,
                years_of_experience: "5-7 years",
                openings: 2,
                posted_date: "2025-04-09T11:00:00",
                deadline: "2025-07-01T23:59:59",
                applications: 25,
                skills: ["Product Management", "Strategy", "Leadership"]
            }
        ]
    },
    22: {
        company_name: "MarketMinds",
        company_logo: "https://via.placeholder.com/100",
        industry_type: "Marketing",
        company_size: "200-500 employees",
        website_url: "https://www.marketminds.com",
        founded_at: "2015",
        About: "MarketMinds specializes in innovative marketing strategies.",
        isverified: true,
        rating: 4.0,
        reviews: 3200,
        jobs: [
            {
                job_id: 24,
                employer_id: 122,
                job_Role: "Digital Marketing Specialist",
                job_type: "Full-time",
                job_category: "Marketing",
                description: "Plan and execute digital marketing campaigns.",
                location_id: 21,
                duration: null,
                stipend: null,
                salary_range_id: 3,
                years_of_experience: "2-4 years",
                openings: 3,
                posted_date: "2025-04-10T12:00:00",
                deadline: "2025-07-05T23:59:59",
                applications: 18,
                skills: ["Digital Marketing", "SEO", "Social Media"]
            }
        ]
    }
};

// Simulated login status
const isLoggedIn = false;

// Sample lookups
const salaryRanges = {
    1: "$30,000 - $40,000/year",
    2: "$50,000 - $60,000/year",
    3: "$40,000 - $50,000/year",
    4: "$70,000 - $90,000/year",
    5: "$60,000 - $80,000/year",
    6: "$55,000 - $75,000/year",
    7: "$50,000 - $70,000/year",
    8: "$80,000 - $100,000/year",
    9: "$90,000 - $120,000/year",
    10: "$100,000 - $130,000/year"
};

const locations = {
    1: "New York",
    2: "Boston",
    3: "San Francisco",
    4: "London",
    5: "Chicago",
    6: "Austin",
    7: "Seattle",
    8: "Miami",
    9: "Toronto",
    10: "Bangalore",
    11: "Singapore",
    12: "Houston",
    13: "Dallas",
    14: "Dubai",
    15: "Tokyo",
    16: "Los Angeles",
    17: "Berlin",
    18: "Sydney",
    19: "Paris",
    20: "Shanghai",
    21: "Mumbai"
};

// Get the company_id from the URL
const urlParams = new URLSearchParams(window.location.search);
const companyId = urlParams.get('company_id');

// Function to calculate days ago
function getDaysAgo(dateString) {
    const postedDate = new Date(dateString);
    const currentDate = new Date('2025-03-28T00:00:00'); // Current date as per your setup
    const diffTime = Math.abs(currentDate - postedDate);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return `${diffDays} days ago`;
}

// Display company details
if (companyId && companies[companyId]) {
    const company = companies[companyId];
    document.getElementById('company-name').textContent = company.company_name;
    document.getElementById('company-logo').src = company.company_logo;
    document.getElementById('industry-type').textContent = company.industry_type;
    document.getElementById('company-size').textContent = company.company_size;
    document.getElementById('website-url').href = company.website_url;
    document.getElementById('website-url').textContent = company.website_url.replace('https://', '');
    document.getElementById('founded-at').textContent = company.founded_at;
    document.getElementById('about').textContent = company.About;
    document.getElementById('verified-badge').style.display = company.isverified ? 'inline-block' : 'none';
    document.getElementById('breadcrumb-company-name').textContent = company.company_name;

    // Dynamically add company action buttons (removed "Follow Company")
    const actionButtons = document.getElementById('action-buttons');
    if (isLoggedIn) {
        actionButtons.innerHTML = `
            <button class="btn-violet" onclick="scrollToJobs()">Apply Now</button>
        `;
    } else {
        actionButtons.innerHTML = `
            <button class="btn-violet" onclick="window.location.href='Login.html'">Login to Apply</button>
        `;
    }

    // Dynamically add job listings
    const jobListings = document.getElementById('job-listings');
    company.jobs.forEach(job => {
        const jobElement = document.createElement('div');
        jobElement.className = 'job-listing';
        jobElement.innerHTML = `
            <div>
                <h4>${job.job_Role}</h4>
                <p><i class="fas fa-map-marker-alt"></i> ${locations[job.location_id]} • ${job.job_type} • ${
                    job.job_type === "Internship" ? `Stipend: $${job.stipend}` : salaryRanges[job.salary_range_id]
                }</p>
            </div>
            <div class="job-actions">
                <button class="btn btn-violet" onclick='viewJob(${JSON.stringify(job)})'>View</button>
                ${isLoggedIn 
                    ? `<button class="btn btn-violet" onclick="applyJob(${job.job_id})">Apply</button>`
                    : `<button class="btn btn-violet" onclick="window.location.href='Login.html'">Login to Apply</button>`}
            </div>
        `;
        jobListings.appendChild(jobElement);
    });
} else {
    document.querySelector('.company-details').innerHTML = '<p>Company not found.</p>';
    document.getElementById('breadcrumb-company-name').textContent = "Company Not Found";
}

// Scroll to jobs section
function scrollToJobs() {
    document.getElementById('jobs-section').scrollIntoView({ behavior: 'smooth' });
}

// View job details in modal
function viewJob(job) {
    const modalContent = document.getElementById('jobDetailsContent');
    const modalTitle = document.getElementById('jobDetailsModalLabel');
    const company = companies[companyId];
    
    // Set modal title
    modalTitle.textContent = `${job.job_Role} - ${company.company_name}`;

    // Set modal content
    const salaryInfo = job.job_type === "Internship" 
        ? `$${job.stipend} Stipend`
        : salaryRanges[job.salary_range_id];
    
    modalContent.innerHTML = `
        <div class="job-overview">
            <div class="job-title">${job.job_Role} - ${company.company_name}</div>
            <div class="company-info">
                <span class="rating"><i class="fas fa-star"></i> ${company.rating}</span>
                <span>${company.reviews} Reviews</span>
            </div>
            <div class="job-meta">
                <span class="job-type">${job.job_type}</span>
                <span class="info">
                    <span><i class="fas fa-briefcase"></i>${job.openings} Openings</span>
                    <span><i class="fas fa-users"></i>${job.applications} Applications</span>
                </span>
            </div>
            <div class="description">${job.description}</div>
            <div class="skills">
                ${job.skills.map(skill => `<span>${skill}</span>`).join(' • ')}
            </div>
            <div class="footer">
                <span>${getDaysAgo(job.posted_date)}</span>
                <button class="btn-save" onclick="saveJob(${job.job_id})">
                    <i class="fas fa-bookmark"></i> Save
                </button>
            </div>
        </div>
        <div class="job-details">
            <div class="detail-item">
                <h6>Category</h6>
                <p>${job.job_category}</p>
            </div>
            <div class="detail-item">
                <h6>Location</h6>
                <p><i class="fas fa-map-marker-alt"></i>${locations[job.location_id]}</p>
            </div>
            <div class="detail-item">
                <h6>Salary</h6>
                <p><i class="fas fa-dollar-sign"></i><span class="highlight">${salaryInfo}</span></p>
            </div>
            ${job.job_type === "Internship" ? `
                <div class="detail-item">
                    <h6>Duration</h6>
                    <p>${job.duration} months</p>
                </div>
            ` : `
                <div class="detail-item">
                    <h6>Experience</h6>
                    <p>${job.years_of_experience}</p>
                </div>
            `}
            <div class="detail-item">
                <h6>Posted</h6>
                <p>${getDaysAgo(job.posted_date)}</p>
            </div>
            <div class="detail-item">
                <h6>Deadline</h6>
                <p class="highlight">${new Date(job.deadline).toLocaleDateString()}</p>
            </div>
        </div>
    `;
    const modal = new bootstrap.Modal(document.getElementById('jobDetailsModal'));
    modal.show();
}

// Apply for job
function applyJob(jobId) {
    alert(`Applying for job ID: ${jobId}`);
}

// Save job
function saveJob(jobId) {
    if (isLoggedIn) {
        alert(`Saved job ID: ${jobId}`);
    } else {
        window.location.href = 'Login.html';
    }
}