-- Sync notesanddoc data to Neon PostgreSQL
BEGIN;

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
    'M9KPTxQyoOJvvDhG7LGNUBdsu0cbuSxBNheN1mxhxz6ZZSkEiCX5DZoLMxs0NqzooVTvvSVEgoe+Wt0uEu9Bqk7HzIe0nVw+Dhda80rNA7p8WYva/aExBYFXd2UPMQfx',
    'YoZqMrPQbnyjLm7hgF58uA==',
    'rV3HxTCYQ4uJsSfIwmRemg=='
  );
INSERT INTO education (degree, institution, field_of_study, start_date, end_date) VALUES (
    '5smSCUOloCtBG4LgthiMOpDrEGa6iELfp1Y/OHfHMHM=',
    'OKlgucArUKbOfSSsIiDwAg==',
    'FHR+z78frA7R3ysEWR3skA==',
    'IOKEqCVC04HlyBXuhuJHhA==',
    '/kts/+9DfMbVfhiFj6WpGQ=='
  );
INSERT INTO education (degree, institution, field_of_study, start_date, end_date) VALUES (
    '9DkA5cMPAWc8QgW7NkhtsQoWQzlMkIwEUPm8HD0W969izMTpL/PsMNvnBhwI7HvS',
    'OKlgucArUKbOfSSsIiDwAg==',
    'W6wh0g5ir6NZV7C6uKzIdHwVaYEJPs3wUMPihM//fMA=',
    '1ZAsg16bWRYCbnc05Kpv6g==',
    'IOKEqCVC04HlyBXuhuJHhA=='
  );

COMMIT;
