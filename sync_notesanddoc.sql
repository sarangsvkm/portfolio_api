-- Sync notesanddoc data to PostgreSQL (Supabase / Neon)
BEGIN;

CREATE TABLE IF NOT EXISTS api_user (
  userid SERIAL PRIMARY KEY,
  username VARCHAR(255) UNIQUE,
  password TEXT,
  role VARCHAR(100)
);
CREATE TABLE IF NOT EXISTS profile (
  id BIGSERIAL PRIMARY KEY,
  name TEXT,
  title TEXT,
  about TEXT,
  email TEXT,
  phone TEXT,
  location TEXT,
  image_url TEXT,
  banner_url TEXT,
  resume_url TEXT
);
CREATE TABLE IF NOT EXISTS social_media (
  id BIGSERIAL PRIMARY KEY,
  platform VARCHAR(255),
  url TEXT,
  profile_id BIGINT REFERENCES profile(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS experience (
  id BIGSERIAL PRIMARY KEY,
  role TEXT,
  company TEXT,
  start_date TEXT,
  end_date TEXT,
  description TEXT
);
CREATE TABLE IF NOT EXISTS education (
  id BIGSERIAL PRIMARY KEY,
  degree TEXT,
  institution TEXT,
  field_of_study TEXT,
  start_date TEXT,
  end_date TEXT,
  description TEXT
);
CREATE TABLE IF NOT EXISTS project (
  id BIGSERIAL PRIMARY KEY,
  title TEXT,
  description TEXT,
  tech_stack TEXT,
  link TEXT,
  project_type VARCHAR(500),
  github_url VARCHAR(1000),
  image_url VARCHAR(1000)
);
CREATE TABLE IF NOT EXISTS skill (
  id BIGSERIAL PRIMARY KEY,
  name TEXT,
  level TEXT,
  category TEXT
);
CREATE TABLE IF NOT EXISTS contact_request (
  id BIGSERIAL PRIMARY KEY,
  name TEXT,
  email TEXT,
  phone TEXT,
  message TEXT,
  otp VARCHAR(50),
  verified BOOLEAN DEFAULT FALSE,
  otp_expiry TIMESTAMP,
  date VARCHAR(100),
  time VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS images (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) UNIQUE,
  type VARCHAR(255),
  data BYTEA
);
CREATE TABLE IF NOT EXISTS system_config (
  id BIGSERIAL PRIMARY KEY,
  config_key VARCHAR(255) UNIQUE NOT NULL,
  config_value TEXT NOT NULL
);

INSERT INTO profile (id, name) VALUES (1, 'init') ON CONFLICT (id) DO NOTHING;

DELETE FROM api_user WHERE username IN ('admin', 'sarangsvkm');
INSERT INTO api_user (username, password, role) VALUES ('sarangsvkm', 'ayoz75/M6WtfEAd6nJ179A==', 'ADMIN');
INSERT INTO api_user (username, password, role) VALUES ('admin', '+LRrQGMsBCBIgCT3f1wVUQ==', 'ADMIN');

UPDATE profile SET 
  name = 'flu/yFrCwKNKcGMf8DTldQ==',
  title = 'S7mxJ/9B7SKSrg0B5nuICMdD2XUgzuAaBhSKxCIVO2K+lJSNmmdICdfSZao4RyRbVYRiFZByDjAR5f526ZlGQZ4KqahLiCks/5RLjyj94hs=',
  about = 'y4UR/TdIKfC3D8W9xTQLwmPTH+O4YHgP0WBQHIUSIs1n4dogZMELny8PR7SHamRpYMh3g3pIU8jdFIDFZL20NGbvjzX2hichigNomfv2ndw/JoA/tR7g/+1dugPHhEB/XJRIPLSzB43hxw3QKwulVkAhxFsAGBSy6ygbRAr3DALIQU9ZN5ly7nFK+UDtzG7XEba2wBSlWlkzDtGUdAnCFKUaKVZBkruS6rG3NJFHIfDvdyJ+mZCKKvPuQVqQiUlBqzooYsj4rZyGe4/AQaZSsMCm27SjIDf5hf8vXDsaTMd+AFvufSDqw3MYYwmnm24u5w/FxBsh2dYVoA9Msm/D0r10Cmc/aPa4DqtbtdbcgHNxzGxKF5lRq3SgLFyCahxIBeVzhJaSyygQba8KwZFd2UKTdD6sKmrpR2EzAqzZ8K/J5mtnZ/z3PqEYHCLdoK1x+f+K2dcT2kONJdrLK3akjICKpmy3+LL2mHGWeXsywh2p9S/QbJFa9XICxPvXZsVS2D2AU7YYrovunXCN+8+CPDaB79lbGXd2/Aec7+3K5artsgUx4R0CYySsWQjWhe8w+XNKzig7aT7wBveZmk4a7YEtwhDACbcTTubHtwZLHv54ddrG07L1bvcadH5tC2tdlSCg0znjI7DOeoYDo43Q+AqxK5g7cMut7XcX0xiCbEKYrQ3e1HnV/3JCkYYuHL6A2jg7VCLydIzJIVl1PacLSEpA3v9lCgzUt2sKtySwwJBSQgf2Tvz29TmJFntuyQC1erBL+nztF6+NmXJERXZ65C7DqTd+Ao8Hv0zfsLjCkeX4pPv67YD0S3oBxGBThK0KaXQ0Iw1PjAWkVjYeBLQes7a3LqnH80S9AiLMRXSaInY=',
  email = '9rijwlMFE0JWgfD1shWPOlf6S6VJ/Ez+0mXk8NYRjK0=',
  phone = 'rdrHUMx/rvZj/0gp+8tOmg==',
  location = 'W2S6GKFsBB756wOhbhQTzOmtln6dVH8CvJ23Ng6rLWY=',
  image_url = '/aJen3kkRsYYkSSAZprtHOcZ1cvWxg3HKcd4hsdS9LtpgtNGjjCv2kzkkAGMP4dq'
WHERE id = 1;

DELETE FROM social_media WHERE profile_id = 1;
INSERT INTO social_media (platform, url, profile_id) VALUES ('portfolio', 'UnFJ0z/VHiT/XUxrNyT1ndEtoNgY4haRhlTv2Vu5Cmw=', 1);
INSERT INTO social_media (platform, url, profile_id) VALUES ('github', 'wd6ztkYy72lzmJw5c9gmIdR4P6REzWkn1nqn7UEvS08=', 1);
INSERT INTO social_media (platform, url, profile_id) VALUES ('linkedin', 'nyAtzF6n9o5kwn9reNNidazUlI6RtRJzdrx+3Wb4XKZzAMktLodccNu+KoM6i9Ku', 1);

DELETE FROM experience;
INSERT INTO experience (role, company, start_date, end_date, description) VALUES (
    'S7mxJ/9B7SKSrg0B5nuICKnGTrCM+HBGgzr38puNZtk=',
    'W42sgfx+/64BckhoOUHLvAx3x7gJYj8xbD3buPoDoMo=',
    'LibXBm5+kDKzp3AKv8M+iA==',
    'g3ITwmhUR36P5OYecV9yfA==',
    'jwQ1biSRo2Il0gGihBI+ghdkgv0vJymdovThI3xprriisilxaPqXABY6SZo8o/nfSy2s/JTYMCo9Zq17xgRkV4FzLmse4PZnBySpEdRTEoNokEEXzyek5n2EnS0lytxDRUK9AtzP69xyQfaWeuQRI5QSWQshB+z0t6OGC1ehShpOgLugyhkdAxZ22QoYkVIv9ljPKDHi5MHdpW8k2g2Wxj+nD2i0+rNK/7+UlkXDvD4V4fKK8ngn6o4XH6wThE/oR3Kfn8EsKeqZ5XiJyuAuCc/FxGCQ7d5Cc1oI03xlMv3bt8eFeUdK+LZbVOsWxhIKvQ0Ob/22FwMOgglKcL7s7rPwCOaz8r+9bArL+iJkV4vp98BcF5K/T7JxS+M814+PEUoBQi/N3abIda8fxO9IyWjRvb9KOF2RrFRVctZSuAS92TO0uJ6H7DtFyIGAZFF/6BItHoae9QJWkt6C0pu6sengjXMsWfO2pcKiPvxisPPDpE/KcQat0BbSspkyIxId+DlKleXtAIGLSS7mKfQoCxNP9TcFnCqnfE9x4f1fzv/BJZzJi01+52Z1ujCR6TQYeQjsf5cVLN21tHndsxJDmL+uKn3RBNuyjzn812PguWtr6TMTLj3HFRNjVJS9vds3K3LcSaLhXtSZ4/t7pqEAHJRgYyVqZqVll0FHdB5bZfDmyY68xUVVxFsvg7MhOqBH4acjXbyYFPxH56+xh4JlHyxbefaweXzOIkx5B9PjDngZ0ndWSRq8H7YzmFxVW1ExVopv2kE8g/8JOgkGdQOtGu7K6EtYVn+jJyY7HfR5zhGYKxUZ11NMDgOLq0hR/J+Xv7TXctLDOq/6w9K0+2rqk6Da38EeSsJSczlBcTdAT8ZEsVTKXz3YS4WhqnyAykblwSSyFtP/GqNdAU795HUDAzRNEqpTfP6kLF3hI+frwvX9Cs/9NBdMwXuQLKhPbBEt+0Ti5Yo4qfmh2TtMoM6bK6O4i3Cnrp+ZaeHclGEkPfuJz43bXz36tWF+o+inM3wbUtVigFdhSL4tVal5h+Apq2qQYlmKWJ5a9yLAVJKF4OltxdO+6ApBpx5wlfodewpJhkXLxXaXdS2xmv1lSUEUx+bvc1mO9NyuFCFmVUBoVxqTMSDplCIUalEKNF5FRChW7s/HhM30uYo2FifbtdhYWxkn4xhimAEKQmKiEUaVA8b9kKfjM9NC/ldOWWgUO4+i0TTRs5jICC45WHsIRP9oJ4YlomFzf1Td2DndBtMT9tNajA+cklWCLilD4xDw9BZkuT+7hn9l4f53oTjbRTIQJmv+FTfGFD7cCd+IsRO2IMLFhn8kZXDWMViVNQ8tpTvMuB1FOkyarfZbV41E6dZVGK+yEM+Qpd9FXQTP9szsEZyxECUqXbu8Chf6/hxErRJ/9odndO1uso8QZqHIIec05uHdrLhWhLZLiYJ2+BXH/al6+Fvfhan3ADZ78jHyLzZUScnspYAp0TwTnuX8ME6mKQ/Lwhf/sbaWbmdaod4PxKHPsPlEWtPyPRu34/6E2XbalrbYjAx7qlhCsBs0fb9z+RNmMKwIdMZw/zj6+ZJ8kKAFf/83O5fpR96XoFcpUamYskBSw2dX+yAwo50QWCDozlte0kQozTn7k1XgeiNLH9Hqxa9WbS1xrFPJjOgIkztuyI8ap0EaZaXrt02XG8KUw5oCvJ00lfFCIEqMv9PHgKve1bb8EVvQEfIZi3LidGWklo3ckt4uHwrfGmdBBZq1LPhAeQzHu3MOIj/IpaGMwtk='
  );
INSERT INTO experience (role, company, start_date, end_date, description) VALUES (
    'W5ZyLQ+hN86BBXJUJ5YiMG7VRLfCXzHSOCZqRQdsioY=',
    'CpTIlFsYR3nZNboDyzoZcMkWZ5ZPl16H9TrEOclcqUc=',
    'yAfs3CzDmlw8LvhA4dbqdA==',
    'ojdo6tWpqm2gQNgw9Ddcfg==',
    'o3AygmAN2Km+s4pHlVm1YsZPApVwyo6IU1sf0aFvkWQnlrVy8cg8z+XjCA/Qi2IjVu2AWKpDjMC2LBQy/pRLIek1V1qvg63U2QOgAT2m/sm5FWP2RFhzhfcBN0/Nd7/0VM2l3gWriLkUdpS4+1UAZZJCQRHBXiNGU1RGQKDhXfSWKugNKqXbGXiDMBTFD5CavrEJdKQQ9sg3nvjJ3OJyZ3JHmv7Ylwj7bpRHpa/MKQVxTwooLLd0KRWyrZrowTio8xHbaGSHzCYnDQhRVfOEaNYyOwb7oZOoFagX6Jc4y1g5FsYzjdYw8Nn4ywXx/INviQ8d69z+8597NGIsYAHq5lutj9xasP+6NDXurtMBA8NE0mH3vpQ6xwUsIeDMD8SuT+g4Z6paNBxnthKCL2ZB5QD1m3/z/6AZ/rlvrVxbslo='
  );
INSERT INTO experience (role, company, start_date, end_date, description) VALUES (
    'L35IgQeo0HbJp7okKuTa1SIrDfjf5gwLuQkLD2fP+BnIEKfiw2QiZy59yYcBQ9/i',
    'VDT6Cgc30WMzkQGa9NVEEdzfIZ0es6SkcsNGv/U0YpU=',
    '6P7+EgrX/rtNFYaWoG1VOA==',
    'XbHv6enGfC6H+RzWh7TdWA==',
    'MNzGGoGVVGlrdr0oap5J73VMSGaOWWd8xuIfBPv4QtzO7pLOE9jytgdrfsr+L11d1PHa7UHGfEsoPwdROYuwVrTbSRPmHKbmXnn5NC/nB1F3tijAMfv/luCeYxhT4/VAGOivB4RUj9E6ZFiNeb+sjx1ctexFiCpdjl+J0NbOmApj/vaDUvT3iAE4q5w9huITT4hA7oVtEyeUIyvqqRfbFBzTsCzciI4VQ1kdKelchRzNX4vw0kn34nrA69OKWWn7N/IsNU5UmIx+bin+YidyxRLHPSmyodbxI8NrUNAP4ZH8vPjyCA8gbt9BXhyA40+FH6+vphiG4AxqfDQ3Pj6JFA=='
  );

DELETE FROM project;
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'hTqtQwX0fSj7D6OXamr6iLxrFILVpSIkxKUZXbC2Df9X6dwFnEdoJ01kfAjh3+zin95XvMhG3NHI+JRcyaXwPvWlcHal6k1wyD2fNYgohf0=',
    'wd6ztkYy72lzmJw5c9gmIWMzUytTYTcYQLfoKUgTnWfZxvdaJThkp0/e6nzMgEea',
    'q99nYAV3Vp9TMX1L3b0L0r3LXHDTyp8UZJrn5VADc6jk0MXpNc8+HuUIhJjdCcFqzYffNEW4kGmK+8zTv+tLWjQ+81La/s88C+7OU4hzyxJRr9eGGw51SKCY1RIwxRwwR7zacRHLGCTSam3xjaMjLEx5N0XqkycqVLe6sAqtggktX9EbAU+qP5s7SoRBjBg6MKH+inIjD8AIWfIclV1oeSwonJIE2/XRI9Wxc6AvNuGwngjoYW8TwfpxfA7yfCO+/6LZKygpe48JcxtACUMxWVXq/VdcUXOVhQFXrMAaERs0Q05Taj6Y/VYIHbh+4lau/F1N9PFrSNBKgruUTlq6ywnAq7u8WXTRXuyfkSegYwIHGLDx5dNuSjIfPhW54cGhDzUlOx0P08YbOctY0RAT69N47Q/HCHZGV3e74BaLuu0vjHFmexwiOjCJ4iGJNDGNi07MlqgZLh6dQOxPIvLyDOc6L1q+GAE/Tm1rEAgO6+CMm4F63Lvo0lIxMpALALrhCPomE13DOOVaJkc0LRBzCi4NaFn30iBF2M7/nNmoQIoHUwvH+FByK/FrqQgDdtAvYv1JowH2csttNUCEsdwSPmf+84pBVwuFZ4nhddvZxYu/hSEJbrjthSnVoVKZbLVqXw+vUZl2SvJ69Y103JNRaFgNYhiD9B8h4elmXueSuaY=',
    'wC/3BsmyOD03TuxDGTlJKUx7MAfFhTQwnkQ3ZA7/DeggTAi19wg6Wgypk3zEPmZriI8nDtSiWvYXikQPNtMmdnTJNuoa8HUuqLWq4rQtdIwNYX6Y4K55Mi5KOIMpWJ+NVh98rbNf8NQPLaKxHRWUpXmVqbtSTaWGNVz/d2gzhGA='
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'XADMfCzm7gL8wdokbQbK4J1qXHpKbQX9hYGKj+w70hSCL4ufk9Ci8e+JirbLDtLS2NTkRMbK1zXksYzQgLFi6Q==',
    'wd6ztkYy72lzmJw5c9gmISHMjPnk3V9k4EvQGsckyK3tR0iXW3r4Z3zytXT11mZQ',
    'Tk+Nofv3XjlzQVvAnNgJb3usPSSgrg6USlygiiOoNKU4jLH3ih6+oMX0Mvaks6tm2lu9pmGWqDnaj6HfaYnjtXYRK1o/uVh77q0bGj0dyFygOjMjM0nncEUBirw6bSBcuDjhX5kdXcO0qwIPS1sIcZNnbgJzM+rqwZYrQ0yd66zCfzGC9KsavFqN/sUOn64I3eFaD+prOLXS+s4AbnzbTz7BvQ87p+0qh1iLne3J1WDcFhu9U/V5uMFPC61B6FyCUagff1pZJWmF54vGlT/jiMLPQCdRY2nLUOutBwOTreTk6Hw3u4RLlg0+ClWUvz9JFGDS4Qcq1QvBwVCZji6CCr5NROJTYg2AO+yXFdyBVPJQ2dgnsXVQbyoo3nJY5u81N7rETxzkbUaK2Tp9VETzdv0+iWvTHqrlE/vBNo64XkQxQTlukOV6T0qq4O+01WHVG2I2feBkIFhgK6fvI8a9H85RagCICPv8sHDaGVDZkDptY6+TJvAujOnQQAb9BpNHGjZ1Uz5sdUijBmqzxp/VloHGSaROsV9jeockpYSZRXO9z4hDyPVsv2ysoLwR8BVL1UVGGYLuHn2ZfPjHeKNQBYJdC900K8iV1nOomb72hBthyO37ohrFjedf2eIGhck3',
    'wC/3BsmyOD03TuxDGTlJKRbHOSaUY6/hDKLdyXDf8LbFtRI1zLW3L84Lb2qE5Aicg9gH5BvPq9suNk9JNpRl2qtPmyy20te6ujKoZGzAaPnnE0J0hBIKh1Ibf55Wgfk6MI3Cp2WYcKWrBfQpCOntnQ=='
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'fG26jIQ35WUjR8wPR43lbojTBbwEOn00xDY69D+9hpirxErHbmoAY7LYiVe54dVVHb0c1EuT/GHcSYWPyl32Yw==',
    'wd6ztkYy72lzmJw5c9gmIYR7TPQLjYmeIxsB2mP6HJhVlvSvqUBk04ZBNOVdJI0ikjrqgR50sSjWyGj8xGlpvg==',
    'N5tRxMNJQ5OY5CiMhAL4Lq9LmQE2SvVQe5mFrUX6StSx6K66DM7sMrozXICNwM6Hqmh5qR5kGBKlwtHOdajIpbtUr+9FI3qynTrIBBJvwAn/ObLp5ZR4ovF6e1pw4X9+APftabnUrEpU1isMI7sO+Pe0TWYgeI3ua+cwR+Xq+6Iuzak4nAhK3UwcDdj83CPRAclDXXE+TI4J726Vv01uTD0b5ysFWqus++wBd/N120OojnL42uHEc8JWuYURP8RX0yLLrdV0LChRi49YQkdY7ppzk/5aJKUL4FJdCV3o50A2KTptPZwZqhHgyujQOZ+kw6chfCZrRaHHp2a0zCIxoKWtsUYAMbWGrTTQ1pdZCRD4S0SB7QzTd+QWGTOyEoPmjouVN9qrt8Kic4N/NJEHlAOylk/X5CxXnzchvWrEjQqUWYvHcpMuTJaIiJEd4ZRLm2PoK3aRL0hDJsIu0/BOWKf7ANMkE6TtdqwUWuzBqJ36i2ZAsidwWEISCv2rs8gc7rg8VQbyAvYtSz9t0rY3QRKI7AR0yXy1KVXeubw87jt3IvYTF0D4xbZNgeMsZQ7HVlu07nf7+ePE4AwkImw6VIehUROinO7QrKx2/qkC76IQc1anQ6biPWc3LQeVcCECmbpkrTW1LkuwMoWG7dGT3A==',
    'jf76XYZV9kPtRiRoA+BLZYCQuXvIFoB9rVLuhc1obJvCejcHBgcmnQPqjHMlzCRF7BaUefDdm7v4QhEtSBTvZzQGvvWT0y9/1c+Q9RWTr7rXcq27tK3sHzELEXaw3IN8J1T22SSSyB/KW112+GderRscVOLeh8vKuneBGgO8zPE='
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'wSIcAEQWnxpcgDANqNrTYbaeCJ1fo/xypmprvEwcbQgbMJMiotLHTdqaBryvtP662ApmbUEhlWB+JCCk0RA4+d7J/MZWvwqNVVo+6KigYSw=',
    'wd6ztkYy72lzmJw5c9gmIdR4P6REzWkn1nqn7UEvS08=',
    'wI5T98R8RGlwbfP3XHmi0TWcsiuS2aS4RWoml48s3zGvP2N3F+garveIIqoxlORybRWeVau97jvt6LO3XHLK012XE2fcmYC2ykVg+GGb6NB2s6m/yatc/0Nmk2BilLhad6I/ChDfHilRLeD9HAhQX5OR1fO+N+4ZZah4aZW2EdYBFlLMYDaK24Rgbjs24WAXYrOhNqYMqsV+6FvcWye1qi1iBMmaB8pNNfZuJF1FoH/1eh4xrr3KKdSLNXnnV50QwE62vP6AI/ZySVxL/3gz9U/99DDlthdjlq9PbUlZubbp9+1kftpp9AzVg1sW7N9Jh/3HJJKuswL6szcRMJ/AoMN1eR4i15vebHClXy9mKg581q/xA9gcNVnbGK3YCrov8O69hy6PJTlbJCMS1zsOQrfAFlQ8oDmxujT2vEiHsTFpTPtogf5yJe2O3K0m7xcYejB5ToVqM0wXe6YQsZE4CA==',
    'wC/3BsmyOD03TuxDGTlJKUx7MAfFhTQwnkQ3ZA7/Dei6XXLpEFleSAFboQZaCEmQeWbVpqNi7JrtODtVcI28+T2P6dwJQADsipdfg+VZIb0wtOnQB7kTWuazGdnxgNaE'
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'YJCb8I1hxszqvakmeTYUxSyvob5F5B3anjx2Y+81G60bK/+rggqeHFavbauVnX3/IivnjfXUMLqWN89z5leeRg==',
    'wd6ztkYy72lzmJw5c9gmIdR4P6REzWkn1nqn7UEvS08=',
    'nhed3YPZgSz0nIboDcHKCDTm2XvPLb238cQG9B/LpPo4UQUJHWfralbkRqGlUbOa3i3UtSvj/wh9F/HI5Owag4kbqZPyLrDMW/yJZao3+JpQ2SEw7ooPNtWhL9iogbrUCrvwuQF6C6T6aWPW5IVgNxb6xwobzEyDjbBenfcdzKPNmlWm2CZr11X5VzYQZFHvXOsl7HiheVRHQXCnM3bMQuggplycv1SSnwAKb3RZaV8vlXmTBKN2Pzeqf6wNXdhAEjPIc8IcIzIZnugzP9/AMg1iH7rergI9BNKg4l1YHJGvgFdFtivWnrjEcOw3Vi/8z1qPEQIHV58PB6HYHN6lONQJZvRYaPjyHKh3N54rnzoNonMypVtS9nV4Ko5rKsN9APWbf/P/oBn+uW+tXFuyWg==',
    '3eodby89FnOUeCQmMfa90DkMhFEIvHj1u2pOUcy/rNu9w0/CVqoeoyVYMVB9XgO9x/2Q8qO0SDtBI7mqavoKY+OsDZ9neNssi/cwH6Qf6Ac='
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'ATUAPErOOPjMTK49xnlhcZTSAMvBTlOjR3RHIT81cghvfnxWX6IrYqJXefPTO14O',
    'wd6ztkYy72lzmJw5c9gmIdR4P6REzWkn1nqn7UEvS08=',
    '4aebT+1BDMzlXPNUMcDAY3ybkrqkbMaocbWNKvHUvlwMbOoLSApP5KzBQBZzFsGvfQ7TiazPGAdgNOgOmmo+cpTy6eeeMAFUSrTNFaf7vZWG/RDjr7lYfEfO3HE7OUNY32lhp2mFABhBB8lpFWB7/6zp1Efmgkp3ONN85zODnQp5o/Ow9EOVKWm0bALtWpsWtr7lDACLymLyuFXEigXwKqXiXi3SEnhfQry292Kb35bxpmpg7Dt5CXmFgQNuO6Ka7aBMJ+s1F9rSzSVXFe7JtfiM68ko+T7iFmZy8pcZehNKi4ATH8Fq8amZ77L3JnOw9wPVxC4bSa5E0amd6Voq1y7fqd2stR2kaKdPHWNf5afy/cZWFBA5C0Dy2S+BTeq2',
    'ul1y6RBZXkgBW6EGWghJkGoG5bHIdtAtpdDJajBlBLXPKOXNy2Ko0w+vHlnNo56uQULntKLOrLqh1l/Rgjejt3XaPONs3BlfEJK+FhDOUpc='
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'SfKGGBRlUmYS0gP57Tm6AbxVB9HiIBpWVQq3AkEWc19gbybUxSA2iscC6sOfId9gdyJnLXLIiVJIc9EdBUND5w==',
    'wd6ztkYy72lzmJw5c9gmIdR4P6REzWkn1nqn7UEvS08=',
    '5qi5hGwjFtgB2syB8bYBnz/NjYzVy5da1poIyshAQHuWb1K0N0/Fn38SnQvkMW9+vFumGvICN8+cJCZcePZ7ycbyqG1+PxS7NubaAjmTG1DXH0nOrasq8HFt1n5lQLkMAmZByCHKz6PmJe03r6n2S9fZEEp7y2hAG4sB3gNCtSvrUXD81XqlCa5KOG2NpWVUE3hWGbVtPWoDVtomfIeHfa4SjwxOU61mOjSBYMuyptJs4ZqOfyorovO4V8VWSqZEWYunW2ezfW5RqG2ArinGlWFDKmNZUk7MgyMIbV8w9SwEdqTRagPfCPwpw3DKAsYn86jCvqEgK3N01/I9oYQ3w29jhre6PSxbgMgZt4mSz4aFhfAGm4FBv0eNDL9t9TlR',
    '4kjzT7/+9rxB7QU+Rr2Fir3DT8JWqh6jJVgxUH1eA72RRP1FpwGcC3Pa2OYvONuR/9b9y/w4LtGqO3Vfak4euHO9AK0HBCQ+O0IX4K8c9Xv8LpOMVV99ioHGmSIP8wSk'
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'G9Iqay3GIMWBu7tJmnMTskn8LhDe0gmHHY8YqWmRG7nCoD+gXt8RPjLo1yPfDszE',
    'wd6ztkYy72lzmJw5c9gmIdR4P6REzWkn1nqn7UEvS08=',
    '42y0U3INI6f6L/iPUSENNmXz9ZO1wIH4SLQOy1qFOlswQybX9XKzD6nSv36IhJrob+Zvgdb4SiKb4A35IoaXbyC8JuVwHQvvkwWHHC8RNvX5K9v5x9P4t2MpugRj2S9Qz7qY/48jL4Ds1HYQzurMkrJbzCcZ9AS7fJetTgVR+d14ZDTYXBdII8RRdP2XUEc0GI3rd0mPvAl+9pA3tIbMcje8jY/3s0qRXL3w0KFIyh2Prwkdel2MyThyIXXrVpDDmbpkrTW1LkuwMoWG7dGT3A==',
    'wC/3BsmyOD03TuxDGTlJKUx7MAfFhTQwnkQ3ZA7/DejDx3NL6UJ9IU8urz4yRwwHtZXEjGn+ly6zJvgur+Z+4rqua1asBn8CZjFPkDmI1T0='
  );
INSERT INTO project (title, link, description, tech_stack) VALUES (
    'lyRljBauxeTx9zJBXhLAeOidvfKygzGvQioPNbbbMPzGpyDSfB6mrIZ/yB1BZ9fq5YTbe3gShvhb4OIRVNTxXA==',
    'wd6ztkYy72lzmJw5c9gmIdR4P6REzWkn1nqn7UEvS08=',
    'jW0PwI9byvPZ8n/L5f/XTDvF9VAPokalE80NV0aPaGMNR1fNgzUgCcSvAAG3F+eksuIriF1FAkzoHwvyLghTRzxWqMdx9M3UqSJCHkeomzBBL41hqeGMVHQprEk8o8bBfDjRuiwqUNVnEtfUFwV0E4IzbWsUv3fqRxIrA1ynDIDVucYFmT+/n6mMkHgTDOeCSTcWWAScFB4500N0uLHHGQ==',
    'z3HyzHqyG2kN0D6pTS6XpPAiEk+rI05pPYJJLAsE/Jyu58aXNt9+wKj2g7hshAhIq0+bLLbS17q6MqhkbMBo+X/nCcBL/jJkiXGU1JYp020='
  );

DELETE FROM skill;
INSERT INTO skill (name, level, category) VALUES (
    'pHzUd4OdHr3bDIAXH12g3g==',
    'LK2rgx/qdcvU+jLJmunqTA==',
    'rgmc5sj2CiyuS4EMVWYQXP/tchJJgWvJATKEaCodapQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    'QyMQc0UVrWHza0DMEE4i2w==',
    'LK2rgx/qdcvU+jLJmunqTA==',
    'rgmc5sj2CiyuS4EMVWYQXP/tchJJgWvJATKEaCodapQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    'U/Yv7K4TuevdkQMBPZ7bZ0lyrwtC6MfAFriKQuj1+C4=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'rgmc5sj2CiyuS4EMVWYQXP/tchJJgWvJATKEaCodapQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    'YRbG3uIlEQCjXUPVLNOneBFG06IOVjkHKyNyePXJ1I8=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'rgmc5sj2CiyuS4EMVWYQXP/tchJJgWvJATKEaCodapQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    'GQYzxZy0xBv1+JwMFc/mkDkn1Lui+gLOJgggL8i8/Ec=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'rgmc5sj2CiyuS4EMVWYQXP/tchJJgWvJATKEaCodapQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    'NviaYeQH953Mq4v0Fq6xDw==',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'hE0roVgzBarp0RM6hLm5Ls+opJhAVKcvTDqL3nuKBtY='
  );
INSERT INTO skill (name, level, category) VALUES (
    'ChfQwcDO9+RicwixspGbdA==',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'hE0roVgzBarp0RM6hLm5Ls+opJhAVKcvTDqL3nuKBtY='
  );
INSERT INTO skill (name, level, category) VALUES (
    'pC611LEQxnLTXI3vOTb7ZbYtfJyMx7kyA8EuwSiLaZw=',
    'LK2rgx/qdcvU+jLJmunqTA==',
    'hE0roVgzBarp0RM6hLm5Ls+opJhAVKcvTDqL3nuKBtY='
  );
INSERT INTO skill (name, level, category) VALUES (
    '/6bvGY2lO8e/pXTj+/Q0/CjOqyU8VDo4KywdndaODyk=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'hE0roVgzBarp0RM6hLm5Ls+opJhAVKcvTDqL3nuKBtY='
  );
INSERT INTO skill (name, level, category) VALUES (
    'b3PCTVSAbhtDIBNLeX9DQhS5TcGLCUfi8cMiZ4m6CnA=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'c93BNPheGk/lW3WnjYGJg/wuk4xVX32KgcaZIg/zBKQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    'Xbd1giojFgZKfNhisD41SA==',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'c93BNPheGk/lW3WnjYGJg/wuk4xVX32KgcaZIg/zBKQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    'vFIJM4bncUE+tDqR+BdC2g==',
    'zHsJ+ZhRE6VyZAWTRlYJvQ==',
    'c93BNPheGk/lW3WnjYGJg/wuk4xVX32KgcaZIg/zBKQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    's7mpuJFcYt1pp9z+7ZDg/6k7CRz533L6KeI+ZGlD3Z36KZTHcHH81QeAGcgAmYei',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'c93BNPheGk/lW3WnjYGJg/wuk4xVX32KgcaZIg/zBKQ='
  );
INSERT INTO skill (name, level, category) VALUES (
    '5e5bEwLK7epH3ev74mEsxQ==',
    'LK2rgx/qdcvU+jLJmunqTA==',
    'SQ9UJao4eUoV9RyU0RjtExSWRXTmAtrqRcFLnU+glQY='
  );
INSERT INTO skill (name, level, category) VALUES (
    '+54BFW31Up8F/3PeC9kjgb/bSL5a3B/B+BFlEHY8YEI=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'SQ9UJao4eUoV9RyU0RjtExSWRXTmAtrqRcFLnU+glQY='
  );
INSERT INTO skill (name, level, category) VALUES (
    'y1O+XK4Vn8rCPMNew7YS8w==',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'SQ9UJao4eUoV9RyU0RjtExSWRXTmAtrqRcFLnU+glQY='
  );
INSERT INTO skill (name, level, category) VALUES (
    'ylvujN6BIzgnqVBGCo6HuQ==',
    'zHsJ+ZhRE6VyZAWTRlYJvQ==',
    'PJOS3MSvovcQfNcdmXdeKJ7wgpraugFh9LnNHa3dSx0='
  );
INSERT INTO skill (name, level, category) VALUES (
    'ifjZIe7cI9Apq4oNwPy186hd+YuQtCA20fGRfPhkfAg=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'r46IDTCyPc/AbILxbeB+xQ=='
  );
INSERT INTO skill (name, level, category) VALUES (
    'rJn1NjM8OwauY0rakEX5dg==',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'r46IDTCyPc/AbILxbeB+xQ=='
  );
INSERT INTO skill (name, level, category) VALUES (
    'tY/xi9gvE+zojebBfdikkRHuzpa3a2uWRZHuGZ5qZYA=',
    'ZSTIFqUJnbAfpir2WQz+7Q==',
    'r46IDTCyPc/AbILxbeB+xQ=='
  );

DELETE FROM education;
INSERT INTO education (degree, institution, field_of_study, start_date, end_date) VALUES (
    'I8T6lsB6H90T5wDnDTdSEUtgHJdUSUbFwpVxO6S6Jqs=',
    'sdVvq4Ew6qjdJ4yGKYfm0eCPLXPdamoxmOumSeNH6a5gcqxoKW19/yfGgyD5KrmP/C6TjFVffYqBxpkiD/MEpA==',
    'M9KPTxQyoOJvvDhG7LGNUICx3/oUajwqkZmRc8WdEmE=',
    'f+HqspC/riafUaB443dlkg==',
    'Ew0mctnp+E/EQmJPj8BSog=='
  );
INSERT INTO education (degree, institution, field_of_study, start_date, end_date) VALUES (
    '5smSCUOloCtBG4LgthiMOpDrEGa6iELfp1Y/OHfHMHM=',
    'OKlgucArUKbOfSSsIiDwAg==',
    'Aul8fNX6d4RpWqijeByP0ZrB2YyE7IvslBQZFynqwcc=',
    'IOKEqCVC04HlyBXuhuJHhA==',
    '/kts/+9DfMbVfhiFj6WpGQ=='
  );
INSERT INTO education (degree, institution, field_of_study, start_date, end_date) VALUES (
    '9DkA5cMPAWc8QgW7NkhtsQoWQzlMkIwEUPm8HD0W969izMTpL/PsMNvnBhwI7HvS',
    'OKlgucArUKbOfSSsIiDwAg==',
    'GdY/wjBEwvW/1qNT6TOzouAKKjJXc29lBdckQDNghb78LpOMVV99ioHGmSIP8wSk',
    '1ZAsg16bWRYCbnc05Kpv6g==',
    'IOKEqCVC04HlyBXuhuJHhA=='
  );

COMMIT;
