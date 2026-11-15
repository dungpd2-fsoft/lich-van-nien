from pathlib import Path
from html import escape
import json
OUT=Path(__file__).parent
LIGHT=dict(bg='#FFFBFE',surface='#F3EDF7',card='#FFFFFF',text='#1D1B20',muted='#49454F',primary='#6750A4',on='#FFFFFF',tonal='#EADDFF',on_tonal='#21005D',line='#CAC4D0',error='#B3261E',error_bg='#F9DEDC')
DARK=dict(bg='#141218',surface='#211F26',card='#2B2930',text='#E6E0E9',muted='#CAC4D0',primary='#D0BCFF',on='#381E72',tonal='#4F378B',on_tonal='#EADDFF',line='#938F99',error='#F2B8B5',error_bg='#601410')
class UI:
 def __init__(self,w=412,h=920,dark=False):self.w=w;self.h=h;self.c=DARK if dark else LIGHT;self.parts=[];self.rect(0,0,w,h,self.c['bg'])
 def rect(self,x,y,w,h,c,r=0,stroke=None):self.parts.append(f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{r}" fill="{c}"'+(f' stroke="{stroke}"' if stroke else '')+'/>')
 def text(self,x,y,s,size=14,c=None,weight=400,anchor='start'):
  self.parts.append(f'<text x="{x}" y="{y}" fill="{c or self.c["text"]}" font-family="Roboto, Noto Sans, sans-serif" font-size="{size}" font-weight="{weight}" text-anchor="{anchor}">{escape(str(s))}</text>')
 def lines(self,x,y,ss,size=14,c=None,step=24):
  for i,s in enumerate(ss):self.text(x,y+i*step,s,size,c)
 def icon(self,x,y,name,c=None):
  paths={'back':'M15 6l-6 6 6 6','next':'M9 6l6 6-6 6','settings':'M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8 M12 2v3 M12 19v3 M2 12h3 M19 12h3 M5 5l2 2 M17 17l2 2 M19 5l-2 2 M7 17l-2 2','calendar':'M5 5h14v15H5z M8 2v6 M16 2v6 M5 10h14','check':'M5 12l4 4L19 6','wifi':'M3 8q9-8 18 0 M6 12q6-5 12 0 M9 16q3-2 6 0 M12 20h.01','info':'M12 10v7 M12 6h.01 M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20','moon':'M20 15A9 9 0 0 1 9 3a9 9 0 1 0 11 12','sun':'M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8 M12 1v3 M12 20v3 M1 12h3 M20 12h3'}
  self.parts.append(f'<path transform="translate({x} {y})" d="{paths.get(name,paths["info"])}" fill="none" stroke="{c or self.c["text"]}" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>')
 def button(self,x,y,w,label,filled=True):
  self.rect(x,y,w,48,self.c['primary'] if filled else self.c['tonal'],24)
  self.text(x+w/2,y+30,label,14,self.c['on'] if filled else self.c['on_tonal'],600,'middle')
 def header(self,title,back=False):
  self.text(24,28,'9:41',12,weight=600);self.text(self.w-24,28,'●  ▰',12,anchor='end')
  if back:self.icon(18,57,'back');self.text(56,76,title,22,weight=600)
  else:self.text(24,76,title,24,weight=600);self.icon(self.w-48,56,'settings')
 def footer(self):self.rect(self.w/2-54,self.h-15,108,4,self.c['text'],2)
 def save(self,name):
  s=f'<svg xmlns="http://www.w3.org/2000/svg" width="{self.w}" height="{self.h}" viewBox="0 0 {self.w} {self.h}" role="img" aria-label="{escape(name)}">'+''.join(self.parts)+'</svg>'
  (OUT/(name+'.svg')).write_text(s);return name
 def month(self,x=20,y=175,w=372,selected=15):
  c=self.c;self.rect(x,y,w,386,c['surface'],28)
  self.icon(x+8,y+12,'back');self.text(x+w/2,y+30,'Tháng 11, 2026',18,weight=600,anchor='middle');self.icon(x+w-32,y+12,'next')
  inset=0 if w<350 else 8
  cw=(w-2*inset)/7
  for i,d in enumerate(['T2','T3','T4','T5','T6','T7','CN']):self.text(x+inset+cw*(i+.5),y+67,d,12,c['muted'],600,'middle')
  for idx in range(42):
   day=idx-5;valid=1<=day<=30;col=idx%7;row=idx//7;cx=x+inset+cw*(col+.5);cy=y+78+row*48
   n=day if valid else (31+day if day<1 else day-30)
   if valid and day==selected:self.rect(cx-22,cy,44,45,c['primary'],16)
   elif valid and day==15:self.rect(cx-22,cy,44,45,'none',16,c['primary'])
   fg=c['on'] if valid and day==selected else c['text'] if valid else c['muted']
   self.text(cx,cy+20,n,16,fg,600 if day in (15,selected) else 400,'middle')
   lunar='7' if day==15 else '—'
   self.text(cx,cy+36,lunar,11,fg if day==selected else c['muted'],400,'middle')
  self.text(x+20,y+375,'Số trên: dương lịch   ·   Số dưới: âm lịch',11,c['muted'])
 def home(self,selected=15):
  c=self.c;self.header('Lịch vạn niên');self.text(24,113,'LỊCH VIỆT NAM  ·  UTC+7',11,c['muted'],600)
  self.button(24,125,108,'Hôm nay',False);self.icon(self.w-145,138,'calendar',c['primary']);self.text(self.w-110,155,'Chọn ngày',14,c['primary'],600)
  self.month(x=12 if self.w<380 else 20,w=self.w-24 if self.w<380 else self.w-40,selected=selected)
  self.text(24,589,'Hôm nay' if selected==15 else 'Ngày đang chọn',12,c['muted'])
  self.rect(20,605,self.w-40,204,c['tonal'],28)
  self.text(40,641,'Chủ nhật, 15 tháng 11',22,c['on_tonal'],600)
  self.text(40,678,'07 / 10',30,c['on_tonal'],600);self.text(170,677,'âm lịch · Bính Ngọ',14,c['on_tonal'])
  self.text(40,705,'Tân Tỵ  ·  Tiết Lập Đông',14,c['on_tonal'])
  self.button(40,734,self.w-80,'Xem chi tiết')
  self.text(24,842,'Nguồn dữ liệu LICH247',12,c['muted']);self.text(24,865,'Thông tin tốt/xấu mang tính tham khảo.',11,c['muted']);self.footer()
 def detail(self):
  c=self.c;self.header('Chi tiết ngày',True)
  self.rect(20,108,self.w-40,168,c['tonal'],28)
  self.text(40,142,'CHỦ NHẬT · THÁNG 11, 2026',12,c['on_tonal'],600);self.text(40,228,'15',80,c['on_tonal'],500)
  self.text(166,179,'07 tháng 10 âm lịch',18,c['on_tonal'],600);self.text(166,210,'Năm Bính Ngọ',14,c['on_tonal']);self.text(166,240,'Ngày Tân Tỵ',14,c['on_tonal'])
  self.text(24,317,'Thông tin ngày',20,weight=600)
  self.rect(20,334,self.w-40,152,c['surface'],24)
  for i,(a,b) in enumerate([('Can chi tháng','Kỷ Hợi'),('Tiết khí','Lập Đông'),('Trực','Phá')]):self.text(40,367+i*45,a,14,c['muted']);self.text(self.w-40,367+i*45,b,16,weight=500,anchor='end')
  self.text(24,524,'Phân loại theo nguồn',20,weight=600)
  for i,(a,b) in enumerate([('Hoàng đạo','Có'),('Hắc đạo','Không'),('Đại cát','Có')]):
   x=20+i*(self.w-48)/3;ww=(self.w-64)/3;self.rect(x,542,ww,72,c['surface'],18);self.text(x+12,567,a,12,c['muted']);self.text(x+12,595,b,18,weight=600)
  self.text(24,655,'Giờ hoàng đạo',20,weight=600)
  for i,(a,b) in enumerate([('Sửu','01–03h'),('Thìn','07–09h'),('Ngọ','11–13h'),('Mùi','13–15h'),('Tuất','19–21h'),('Hợi','21–23h')]):
   x=20+(i%3)*((self.w-48)/3);yy=674+(i//3)*64;ww=(self.w-64)/3;self.rect(x,yy,ww,54,c['surface'],16);self.text(x+12,yy+22,a,14,weight=600);self.text(x+12,yy+42,b,12,c['muted'])
  self.lines(24,831,['Nguồn LICH247','Thông tin tốt/xấu chỉ để tham khảo.'],12,c['muted'],22);self.footer()
 def settings(self):
  c=self.c;self.header('Cài đặt',True);self.text(24,126,'Cá nhân hóa',14,c['primary'],600)
  for y,icon,title,sub in [(147,'moon','Giao diện','Theo hệ thống'),(267,'calendar','Lịch Việt Nam','Ngày hôm nay theo múi giờ UTC+7'),(409,'info','Thông tin ứng dụng & nguồn','Phiên bản, dữ liệu và quyền riêng tư')]:
   self.rect(20,y,self.w-40,98,c['surface'],24);self.icon(40,y+24,icon,c['primary']);self.text(80,y+37,title,16,weight=600);self.text(80,y+65,sub,12,c['muted']);self.icon(self.w-52,y+27,'next')
  self.rect(20,555,self.w-40,126,c['tonal'],24);self.lines(40,588,['Nhẹ nhàng trong từng thao tác','Chuyển động theo thiết lập hệ thống.','Dữ liệu lịch đã tải có thể xem lại khi offline.'],13,c['on_tonal'],28);self.footer()
 def about(self):
  c=self.c;self.header('Thông tin ứng dụng',True);self.rect(self.w/2-36,120,72,72,c['tonal'],24);self.icon(self.w/2-12,144,'calendar',c['primary']);self.text(self.w/2,233,'Lịch vạn niên',26,weight=600,anchor='middle');self.text(self.w/2,261,'Thông tin phiên bản từ bản cài đặt',12,c['muted'],anchor='middle')
  self.rect(20,305,self.w-40,201,c['surface'],24);self.text(40,340,'Nguồn dữ liệu',18,weight=600);self.lines(40,372,['LICH247 · Lịch Việt Nam UTC+7','Can chi, tiết khí, trực và giờ hoàng đạo','được hiển thị theo nguồn dữ liệu.','Nội dung tốt/xấu chỉ mang tính tham khảo.'],13,c['muted'],29)
  self.button(20,537,self.w-40,'Mở trang nguồn LICH247');self.button(20,604,self.w-40,'Chính sách quyền riêng tư',False);self.text(24,691,'Liên kết quyền riêng tư bật khi có trang chính thức.',11,c['muted']);self.footer()
 def dialog(self,kind):
  self.settings() if kind=='theme' else self.home();self.rect(0,0,self.w,self.h,'#00000099');c=self.c;x=24;w=self.w-48;y=220
  self.rect(x,y,w,420 if kind=='date' else 350,c['surface'],28)
  self.text(x+24,y+43,'Chọn ngày dương lịch' if kind=='date' else 'Chọn giao diện',23,weight=500)
  if kind=='date':
   self.text(x+24,y+80,'Tra cứu lịch theo ngày bạn chọn',13,c['muted'])
   for i,(lab,val) in enumerate([('Ngày','30'),('Tháng','02'),('Năm','2026')]):
    xx=x+24+i*(w-48)/3;ww=(w-64)/3;self.rect(xx,y+110,ww,70,'none',8,c['primary']);self.text(xx+12,y+132,lab,12,c['primary']);self.text(xx+12,y+162,val,22)
   self.lines(x+24,y+221,['Ngày không hợp lệ. Vui lòng kiểm tra','ngày, tháng, năm.'],13,c['error'],22)
   self.text(x+24,y+282,'Ví dụ trạng thái xác thực lỗi',11,c['muted'])
   self.button(x+24,y+340,100,'Hủy',False);self.button(x+140,y+340,w-164,'Xem ngày')
  else:
   for i,s in enumerate(['Theo hệ thống','Sáng','Tối']):
    yy=y+97+i*56;self.rect(x+24,yy-15,20,20,'none',10,c['primary']);
    if i==0:self.rect(x+29,yy-10,10,10,c['primary'],5)
    self.text(x+60,yy+1,s,16)
   self.button(x+24,y+275,100,'Hủy',False);self.button(x+140,y+275,w-164,'Áp dụng')
 def state(self,kind):
  c=self.c;self.header('Chi tiết ngày',True);self.text(24,132,'Chủ nhật, 15/11/2026',24,weight=600)
  if kind=='offline':
   self.rect(20,159,self.w-40,128,c['tonal'],24);self.icon(40,179,'wifi',c['primary']);self.text(76,199,'Bạn đang xem dữ liệu đã lưu',14,c['on_tonal'],600);self.text(40,230,'Lưu lúc 20:24 · 15/11/2026',12,c['on_tonal']);self.text(40,262,'Thử lại',14,c['primary'],600)
   self.rect(20,311,self.w-40,138,c['surface'],24);self.text(40,352,'Âm lịch 07 / 10 / 2026',24,weight=600);self.lines(40,388,['Bính Ngọ · Ngày Tân Tỵ','Tiết khí Lập Đông · Trực Phá'],14,c['muted'])
  elif kind=='loading':
   self.text(24,190,'Đang tải thông tin ngày…',16,c['muted']);
   for y,w in [(228,self.w-40),(258,self.w-90),(332,self.w-40),(362,self.w-70),(440,self.w-40)]:self.rect(20,y,w,20,c['surface'],10)
  else:
   self.rect(20,190,self.w-40,278,c['error_bg'],28);self.icon(44,219,'info',c['error']);self.text(44,279,'Chưa tải được thông tin ngày',18,c['error'],600);self.lines(44,317,['Kiểm tra kết nối rồi thử lại.','Bạn vẫn có thể xem lịch dương.'],14,c['error'],26);self.button(44,385,self.w-88,'Thử lại')
  self.footer()
 def tablet(self):
  c=self.c;self.header('Lịch vạn niên');self.text(24,111,'LỊCH VIỆT NAM · UTC+7',12,c['muted'],600)
  self.button(24,132,116,'Hôm nay',False);self.button(156,132,132,'Chọn ngày',False)
  self.month(24,204,548)
  self.text(40,631,'Hôm nay',12,c['primary'],600);self.text(40,663,'Chủ nhật, 15 tháng 11, 2026',23,weight=600)
  self.rect(608,132,self.w-632,self.h-168,c['surface'],28);self.text(636,176,'Chi tiết ngày',20,weight=600);self.rect(632,201,self.w-680,144,c['tonal'],24);self.text(656,244,'07 / 10 âm lịch',30,c['on_tonal'],600);self.text(656,278,'Bính Ngọ · Ngày Tân Tỵ',18,c['on_tonal']);self.text(656,311,'Chủ nhật, 15/11/2026',14,c['on_tonal'])
  self.text(636,390,'Thông tin ngày',20,weight=600);self.lines(636,425,['Can chi tháng: Kỷ Hợi','Tiết khí: Lập Đông · Trực: Phá','Hoàng đạo: Có · Hắc đạo: Không · Đại cát: Có'],15,c['muted'],30)
  self.text(636,557,'Giờ hoàng đạo',20,weight=600);self.lines(636,594,['Sửu 01–03h · Thìn 07–09h · Ngọ 11–13h','Mùi 13–15h · Tuất 19–21h · Hợi 21–23h'],15,c['muted'],30);self.text(636,self.h-63,'Nguồn LICH247 · Thông tin tham khảo',12,c['muted']);self.footer()
files=[]
for dark in [False,True]:
 theme='dark' if dark else 'light'
 for name,method in [('S01-calendar','home'),('S11-day-detail','detail'),('S02-settings','settings'),('S12-about','about')]:
  u=UI(dark=dark);getattr(u,method)();files.append(u.save(name+'-'+theme))
 for kind in ['date','theme']:
  u=UI(dark=dark);u.dialog(kind);files.append(u.save('D01-date-error-'+theme if kind=='date' else 'D02-theme-'+theme))
 u=UI(1280,840,dark);u.tablet();files.append(u.save('S01-tablet-'+theme))
for kind in ['loading','offline','error']:
 u=UI();u.state(kind);files.append(u.save('state-'+kind))
u=UI(360,920);u.home();files.append(u.save('S01-compact-360'))
u=UI();u.home();u.rect(24,820,364,64,'#322F35',16);u.text(44,858,'Đã cập nhật giao diện',14,'#F5EFF7');files.append(u.save('T01-toast'))
# Separate today from selected day, without inventing unverified lunar data.
u=UI();u.home(selected=16)
u.parts=[v.replace('Chủ nhật, 15 tháng 11','Thứ hai, 16 tháng 11').replace('07 / 10','— / —').replace('âm lịch · Bính Ngọ','Chưa tải dữ liệu âm').replace('Tân Tỵ  ·  Tiết Lập Đông','Chọn Xem chi tiết để tra cứu ngày này.') for v in u.parts]
files.append(u.save('S01-selected-not-today'))
# Verified leap-month example from saved API evidence.
u=UI();u.detail()
repls={'CHỦ NHẬT · THÁNG 11, 2026':'THỨ SÁU · THÁNG 7, 2025','>15</text>':'>25</text>','07 tháng 10 âm lịch':'01 tháng 6 nhuận','Năm Bính Ngọ':'Năm Ất Tỵ','Ngày Tân Tỵ':'Ngày Quý Mùi','Kỷ Hợi':'Quý Mùi','Lập Đông':'Đại Thử','>Phá</text>':'>Kiến</text>','Sửu':'Dần','01–03h':'03–05h','Thìn':'Mão','07–09h':'05–07h','Ngọ':'Tỵ','11–13h':'09–11h','>Mùi</text>':'>Thân</text>','13–15h':'15–17h'}
for a,b in repls.items():u.parts=[v.replace(a,b) for v in u.parts]
# Correct the Dai Cat flag only, not the Hoang Dao flag.
u.parts=[v.replace('>Có</text>','>Không</text>') if 'y="595"' in v and float(v.split('x="')[1].split('"')[0])>250 else v for v in u.parts]
files.append(u.save('S11-leap-month'))
(OUT/'manifest.json').write_text(json.dumps({'status':'local-preview-not-uploaded-to-figma','palette':'Material 3 baseline purple','screens':files,'notes':['Calendar has only verified lunar day 15/11/2026; other cells use missing-data dash.','These SVGs are local handoff assets; imported SVG hierarchy is not Figma Auto Layout or prototype.','Actual font depends on installed Roboto/Noto Sans fallback.']},ensure_ascii=False,indent=2))
html='<!doctype html><meta charset="utf-8"><title>Lịch vạn niên — UI v1</title><style>body{font:16px sans-serif;background:#ebe7f0;color:#1d1b20;margin:40px}h1{font-size:32px}.grid{display:flex;flex-wrap:wrap;gap:32px}figure{margin:0}figcaption{padding:12px 0;font-size:14px}img{width:360px;border-radius:24px;box-shadow:0 8px 30px #20103016}figure.tablet img{width:900px}</style><h1>Lịch vạn niên / Material 3</h1><p>UI v1 · Bản xem trước cục bộ, chưa cập nhật lên Figma do hạn mức MCP. Lịch minh họa trạng thái tải một phần.</p><div class="grid">'
for f in files:html+=f'<figure class="{"tablet" if "tablet" in f else "phone"}"><figcaption>{f}</figcaption><img src="{f}.svg"></figure>'
html+='</div>';(OUT/'index.html').write_text(html)
print(f'Generated {len(files)} SVG screens')
