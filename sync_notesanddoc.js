import fs from 'fs';
import crypto from 'crypto';

const KEY = Buffer.from('SRG_AES_KEY_2026', 'utf8');

function enc(t) {
  if (!t) return null;
  const c = crypto.createCipheriv('aes-128-ecb', KEY, null);
  return c.update(String(t), 'utf8', 'base64') + c.final('base64');
}

function sqlStr(val) {
  if (val === null || val === undefined) return 'NULL';
  return "'" + String(val).replace(/'/g, "''") + "'";
}

const resume = JSON.parse(fs.readFileSync('e:/SRG/portfolio-frontend/public/data/resume-data.json', 'utf8'));

let sql = '-- Sync notesanddoc data to Neon PostgreSQL\nBEGIN;\n\n';

// 1. Profile
sql += `UPDATE profile SET 
  name = ${sqlStr(enc(resume.profile.name))},
  title = ${sqlStr(enc(resume.profile.title))},
  about = ${sqlStr(enc(resume.profile.about))},
  email = ${sqlStr(enc(resume.profile.email))},
  phone = ${sqlStr(enc(resume.profile.phone))},
  location = ${sqlStr(enc(resume.profile.location))},
  image_url = ${sqlStr(enc(resume.profile.imageUrl))}
WHERE id = 1;\n\n`;

// 2. Social Media
sql += 'DELETE FROM social_media WHERE profile_id = 1;\n';
for (const sm of resume.profile.socialMediaLinks) {
  sql += `INSERT INTO social_media (platform, url, profile_id) VALUES (${sqlStr(sm.platform)}, ${sqlStr(enc(sm.url))}, 1);\n`;
}
sql += '\n';

// 3. Experience
sql += 'DELETE FROM experience;\n';
for (const exp of resume.experiences) {
  sql += `INSERT INTO experience (role, company, start_date, end_date, description) VALUES (
    ${sqlStr(enc(exp.role))},
    ${sqlStr(enc(exp.company))},
    ${sqlStr(enc(exp.startDate))},
    ${sqlStr(enc(exp.endDate))},
    ${sqlStr(enc(exp.description))}
  );\n`;
}
sql += '\n';

// 4. Projects
sql += 'DELETE FROM project;\n';
for (const p of resume.projects) {
  sql += `INSERT INTO project (title, link, description, tech_stack) VALUES (
    ${sqlStr(enc(p.title))},
    ${sqlStr(enc(p.link))},
    ${sqlStr(enc(p.description))},
    ${sqlStr(enc(p.techStack))}
  );\n`;
}
sql += '\n';

// 5. Skills
sql += 'DELETE FROM skill;\n';
for (const s of resume.skills) {
  sql += `INSERT INTO skill (name, level, category) VALUES (
    ${sqlStr(enc(s.name))},
    ${sqlStr(enc(s.level))},
    ${sqlStr(enc(s.category))}
  );\n`;
}
sql += '\n';

// 6. Education
sql += 'DELETE FROM education;\n';
for (const ed of resume.educations) {
  sql += `INSERT INTO education (degree, institution, field_of_study, start_date, end_date) VALUES (
    ${sqlStr(enc(ed.degree))},
    ${sqlStr(enc(ed.institution))},
    ${sqlStr(enc(ed.fieldOfStudy))},
    ${sqlStr(enc(ed.startDate))},
    ${sqlStr(enc(ed.endDate))}
  );\n`;
}
sql += '\nCOMMIT;\n';

fs.writeFileSync('e:/SRG/portfolio-api/sync_notesanddoc.sql', sql, 'utf8');
console.log('SQL generated successfully! Size:', sql.length, 'bytes');

